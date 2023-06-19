package com.itidng.contorller;

import com.itidng.dto.DishDto;
import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.service.DishService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {

    @Autowired
    private DishService mapper;


    /**
     * 菜品页面展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<PageList> dishList(@PathParam("page") int page, @PathParam("pageSize") int pageSize, @PathParam("name") String name) {
        PageList pageList = mapper.getPages(page, pageSize, name);
        return new R<>(1, pageList, "菜品展示成功");
    }

    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto) {
        mapper.saveDish(dishDto);
        return new R<>(1, null, "菜品添加成功");
    }

    /**
     * 删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids) {
        mapper.deleteDish(ids);
        return new R<>(1, null, "删除成功");
    }


    /**
     * 修改菜品时回显菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDishByID(@PathVariable("id") Long id) {
        DishDto dishDto = mapper.getDishDtoById(id);
        return new R<>(1, dishDto, "菜品信息回显成功");

    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        mapper.updateDish(dishDto);
        return new R<>(1, null, "菜品修改成功");
    }

    /**
     * 修改菜品状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> setDishStatus0(@RequestParam("ids") List<Long> ids) {
        mapper.setDishStatus(ids, 0);
        return new R<>(1, null, "菜品停售成功");
    }

    /**
     * 修改菜品状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> setDishStatus1(@RequestParam("ids") List<Long> ids) {
        mapper.setDishStatus(ids, 1);
        return new R<>(1, null, "菜品起售成功");
    }


    /**
     * 客户端菜品展示
     *
     * @param categoryId
     * @return
     */
    @GetMapping("list")
    public R<List<DishDto>> dishList(Long categoryId) {
        List<DishDto> dishDtoList = mapper.getDishes(categoryId);
        return new R<>(1, dishDtoList, "菜品展示成功");
    }
}
