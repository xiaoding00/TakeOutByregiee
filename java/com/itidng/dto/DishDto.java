package com.itidng.dto;

import com.itidng.pojo.Dish;
import com.itidng.pojo.DishFlavor;
import lombok.Data;

import java.util.List;


@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;

    private String categoryName;
}
