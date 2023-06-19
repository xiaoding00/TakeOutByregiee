package com.itidng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.dto.MealDto;
import com.itidng.mapper.SetMealMapper;
import com.itidng.pojo.Setmeal;
import com.itidng.pojo.SetmealDish;
import com.itidng.service.SetMealDishService;
import com.itidng.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;

    /**
     * 添加套餐业务功能
     * @param mealDto
     */
    @Transactional
    public void addMeal(MealDto mealDto) {
        this.save(mealDto);
        List<SetmealDish> setmealDishes = mealDto.getSetmealDishes();
        List<SetmealDish> setMealDishList= setmealDishes.stream().map((item)->{
            item.setSetmealId(mealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setMealDishList);
    }

    /**
     * 修改套餐操作
     * @param mealDto
     */
    @Transactional
    public void updateMeal(MealDto mealDto) {
       this.updateById(mealDto);
        Long SetMealId = mealDto.getId();
        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,SetMealId);
        //先删除meal_dish表中的对应数据，再进行添加，从而达到修改操作
        setMealDishService.remove(wrapper);
        List<SetmealDish> setmealDishes = mealDto.getSetmealDishes();
        setmealDishes=  setmealDishes.stream().map((item)->{
            item.setSetmealId(SetMealId);
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setmealDishes);
    }

    /**
     * 修改套餐时的回显操作
     * @param id
     * @return
     */
    @Transactional
    public MealDto getMeal(Long id) {
        LambdaQueryWrapper<Setmeal> wrapper=new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SetmealDish> mealDtoWrapper=new LambdaQueryWrapper<>();
        mealDtoWrapper.eq(SetmealDish::getSetmealId,id);
        wrapper.eq(Setmeal::getId,id);
        Setmeal setmeal = this.getOne(wrapper);
        MealDto mealDto=new MealDto();
        BeanUtils.copyProperties(setmeal,mealDto);
        List<SetmealDish> dishList = setMealDishService.list(mealDtoWrapper);
        mealDto.setSetmealDishes(dishList);
        return mealDto;
    }
}
