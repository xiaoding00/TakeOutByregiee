package com.itidng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itidng.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
