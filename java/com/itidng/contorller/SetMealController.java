package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itidng.dto.MealDto;
import com.itidng.pojo.Category;
import com.itidng.pojo.Setmeal;
import com.itidng.pojo.SetmealDish;
import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.service.CategoryService;
import com.itidng.service.SetMealDishService;
import com.itidng.service.SetMealService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetMealController {

    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetMealService mapper;

    /**
     * 展示套餐信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    public R<PageList> mealList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, String name) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            wrapper.like(Setmeal::getName, name);
        }
        Page<Setmeal> mealPage = new Page<>(page, pageSize);
        Page<MealDto> mealDtoPage = new Page<>();
        mapper.page(mealPage, wrapper);
        BeanUtils.copyProperties(mealPage, mealDtoPage, "records");
        List<Setmeal> records = mealPage.getRecords();
        List<MealDto> mealDtoList = records.stream().map((item) -> {
            MealDto mealDto = new MealDto();
            BeanUtils.copyProperties(item, mealDto);
            Category category = categoryService.getById(item.getCategoryId());
            mealDto.setCategoryName(category.getName());
            return mealDto;
        }).collect(Collectors.toList());

        mealDtoPage.setRecords(mealDtoList);
        PageList pageList = new PageList();
        pageList.setTotal(mealDtoPage.getTotal());
        pageList.setRecords(mealDtoPage.getRecords());
        pageList.setPages(mealDtoPage.getPages());
        return new R<>(1, pageList, "套餐展示成功");
    }

    /**
     * 添加套餐
     *
     * @param mealDto
     * @return
     */
    @PostMapping
    public R<String> addMeal(@RequestBody MealDto mealDto) {
        //mealDto里面是存有setMeal对象数据的。
        mapper.addMeal(mealDto);
        return new R<>(1, null, "套餐添加成功");
    }

    /**
     * 修改套餐回显功能呢
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<MealDto> getMealById(@PathVariable("id") Long id) {
        MealDto meal = mapper.getMeal(id);
        return new R<>(1, meal, "套餐信息回显成功");
    }

    /**
     * 修改套餐
     *
     * @param mealDto
     * @return
     */
    @PutMapping
    public R<String> updateSetMeal(@RequestBody MealDto mealDto) {
        mapper.updateMeal(mealDto);
        return new R<>(1, null, "套餐修改成功");
    }

    /**
     * 套餐删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteSetMeal(@RequestParam("ids") List<Long> ids) {
        //根据套餐id,先判断套餐是否正在起售，查询套餐关联的菜品然后直接删除
        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        List<Setmeal> setmealList = mapper.listByIds(ids);
        for (Setmeal setmeal : setmealList) {
            Long id = setmeal.getId();
            if (setmeal.getStatus() ==1){
                return new R<>(0,"该套餐处于起售状态","该套餐处于起售状态");
            }
            mapper.removeById(setmeal);
            wrapper.eq(SetmealDish::getSetmealId,id);
            List<SetmealDish> setmealDishes = setMealDishService.list(wrapper);
            setMealDishService.removeBatchByIds(setmealDishes);
        }
        return new R<>(1,null,"");
    }

    /**
     * 套餐停售
     * @param ids
     * @return
     */
    @PostMapping("status/0")
    public R<String> closeStatus(@RequestParam("ids") List<Long> ids){
        List<Setmeal> setmealList = mapper.listByIds(ids);
         setmealList=  setmealList.stream().map((item)->{
            item.setStatus(0);
            return item;
        }).collect(Collectors.toList());
     mapper.updateBatchById(setmealList);
        return new R<>(1,null,"套餐停售成功");
    }

    /**
     * 套餐起售
     * @param ids
     * @return
     */
    @PostMapping("status/1")
    public R<String> openStatus(@RequestParam("ids") List<Long> ids){
        List<Setmeal> setmealList = mapper.listByIds(ids);
        setmealList=  setmealList.stream().map((item)->{
            item.setStatus(1);
            return item;
        }).collect(Collectors.toList());
        mapper.updateBatchById(setmealList);
        return new R<>(1,null,"套餐起售成功");
    }

    /**
     * 客户端套餐展示
     *
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("list")
    public R<List<Setmeal>> getCategoryName(@PathParam("categoryId") Long categoryId, @PathParam("status") Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Setmeal::getCategoryId, categoryId);
        wrapper.eq(Setmeal::getStatus, status);
        List<Setmeal> list = mapper.list(wrapper);
        return new R<>(1, list, "分类查询成功");
    }


    /**
     * 客户端套餐展示
     *
     * @param id
     * @return
     */
    @GetMapping("dish/{setmealId}")
    public R<MealDto> getById(@PathVariable("setmealId") Long id) {
        MealDto meal = mapper.getMeal(id);
        return new R<>(1, meal, "套餐展示成功");
    }


}
