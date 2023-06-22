package com.itidng.dto;

import com.itidng.pojo.Setmeal;
import com.itidng.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class MealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
