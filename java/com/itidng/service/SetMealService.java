package com.itidng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itidng.dto.MealDto;
import com.itidng.pojo.Setmeal;

public interface SetMealService extends IService<Setmeal> {
    void addMeal(MealDto mealDto);

    void updateMeal(MealDto mealDto);
    MealDto getMeal(Long id);
}
