package com.itidng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itidng.dto.DishDto;
import com.itidng.pojo.Dish;
import com.itidng.result.PageList;

import java.util.List;

public interface DishService extends IService<Dish> {
    /**
     * 添加菜品
     *
     * @param dishDto
     */
    void saveDish(DishDto dishDto);

    /**
     * 删除菜品
     *
     * @param ids
     */
    void deleteDish(List<Long> ids);

    /**
     * 通过菜品id查询菜品
     *
     * @param id
     * @return
     */
    DishDto getDishDtoById(Long id);

    /**
     * 修改菜品
     *
     * @param dishDto
     */
    void updateDish(DishDto dishDto);

    /**
     * 设置起售，停售
     *
     * @param ids
     * @param status
     */
    void setDishStatus(List<Long> ids, int status);

    /**
     * 展示菜品列表
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    PageList getPages(int page, int pageSize, String name);

    /**
     * 客户端菜品展示
     * @param categoryId
     * @return
     */
    List<DishDto> getDishes(Long categoryId);
}
