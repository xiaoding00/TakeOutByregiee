package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itidng.pojo.ShoppingCart;
import com.itidng.result.CurrentId;
import com.itidng.result.R;
import com.itidng.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService mapper;

    @GetMapping("list")
    public R<List<ShoppingCart>> ShoppingCartList() {
        Long userId = CurrentId.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = mapper.list();
        return new R<>(1, shoppingCarts, "购物车展示完成");
    }

    /**
     * 客户端购物车添加商品
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public R<String> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        //判断餐品类型
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(dishId == null, ShoppingCart::getSetmealId, setmealId);
        wrapper.eq(setmealId == null, ShoppingCart::getDishId, dishId);
        //从数据库中取值判断购物车是否存在
        ShoppingCart dish = mapper.getOne(wrapper);
        //存在则数量加1.
        if (dish != null) {
            dish.setNumber(dish.getNumber()+1);
            mapper.updateById(dish);
            return new R<>(1, "菜品添加成功", "菜品添加成功");
        }
        shoppingCart.setUserId(CurrentId.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());
        mapper.save(shoppingCart);
        return new R<>(1, "菜品添加成功", "菜品添加成功");
    }


    /**
     * 客户端购物车清空
     *
     * @return
     */
    @DeleteMapping("clean")
    public R<String> deleteShoppingCart() {
        Long userId = CurrentId.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        mapper.remove(wrapper);
        return new R<>(1, "购物车清空成功", "购物车清空成功");
    }

    /**
     * 客户端删减菜品
     *
     * @param map
     * @return
     */
    @PostMapping("sub")
    public R<String> deleteOneDish(@RequestBody Map<String, Long> map) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long dishId = map.get("dishId");
        Long setmealId = map.get("setmealId");
        wrapper.eq(dishId == null, ShoppingCart::getSetmealId, setmealId);
        wrapper.eq(setmealId == null, ShoppingCart::getDishId, dishId);
        ShoppingCart one = mapper.getOne(wrapper);
        Integer number = one.getNumber();
        if (number > 1) {
            number = number - 1;
            one.setNumber(number);
            mapper.updateById(one);
            return new R<>(1, "菜品删减一个成功", "菜品删减一个成功");
        }
        mapper.removeById(one);
        return new R<>(1, "菜品删除成功", "菜品删除成功");
    }

}
