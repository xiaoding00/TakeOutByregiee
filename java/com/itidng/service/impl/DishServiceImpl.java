package com.itidng.service.impl;


import ch.qos.logback.core.testUtil.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.dto.DishDto;
import com.itidng.mapper.DishMapper;
import com.itidng.pojo.Dish;
import com.itidng.pojo.DishFlavor;
import com.itidng.result.PageList;
import com.itidng.service.CategoryService;
import com.itidng.service.DishFlavorsService;
import com.itidng.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorsService flavorsService;

    /**
     * 添加菜品
     *
     * @param dishDto
     */
    //事务管理，有一条sql出错事务回滚。
    @Transactional
    public void saveDish(DishDto dishDto) {

        dishDto.setCode((long) RandomUtil.getPositiveInt());
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> dishFlavors = dishDto.getFlavors().stream().map((item) ->
        {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        flavorsService.saveBatch(dishFlavors);
    }

    /**
     * 删除菜品
     *
     * @param ids
     */
    @Transactional
    public void deleteDish(List<Long> ids) {
        this.removeBatchByIds(ids);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        for (Long id : ids) {
            wrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list = flavorsService.list(wrapper);
            if (list.size() != 0) {
                flavorsService.removeBatchByIds(list);
            }
        }
    }

    /**
     * 通过菜品id查询菜品
     *
     * @param id
     * @return
     */
    public DishDto getDishDtoById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorList = flavorsService.list(wrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     */
    //开启事务
    @Transactional
    public void updateDish(DishDto dishDto) {
        //菜品属性的修改
        this.updateById(dishDto);
        //菜品口味的修改,先删除之前的口味数据，再进行存储新的口味，从而达到修改目的。
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        flavorsService.remove(wrapper);
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        flavorsService.saveBatch(dishFlavors);
    }


    /**
     * 设置起售，停售
     *
     * @param ids
     * @param status
     */
    @Transactional
    public void setDishStatus(List<Long> ids, int status) {
        List<Dish> dishes = ids.stream().map((id) -> {
            Dish dish = this.getById(id);
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        this.updateBatchById(dishes);
    }


    /**
     * 展示菜品列表
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Transactional
    public PageList getPages(int page, int pageSize, String name) {

        //菜品管理页面的展示分析：
        //需要展示Dish类型外加----菜品分类名称----由Dish中的categoryId查询categoryName
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //查询dish中的数据
        if (null != name) {
            wrapper.like(Dish::getName, name);
        }

        wrapper.orderByDesc(Dish::getUpdateTime);
        this.page(dishPage, wrapper);
        //将dish中的数据copy到dishDto中，records不拷贝。调用的是Spring所提供的方法.
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> records = dishPage.getRecords();
        //用stream流的方式遍历records中的数据
        List<DishDto> dishDtoRecords = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //由Dish中的categoryId查询categoryName
            Long categoryId = dishDto.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoRecords);
        PageList pageList = new PageList();
        pageList.setRecords(dishDtoPage.getRecords());
        pageList.setTotal(dishDtoPage.getTotal());
        return pageList;
    }

    /**
     * 客户端菜品展示
     *
     * @param categoryId
     * @return
     */
    @Transactional
    public List<DishDto> getDishes(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus, 1);
        wrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> list = this.list(wrapper);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            Long dishId = item.getId();
            BeanUtils.copyProperties(item, dishDto);
            dishFlavorWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = flavorsService.list(dishFlavorWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }
}
