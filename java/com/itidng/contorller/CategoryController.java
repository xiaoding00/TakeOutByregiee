package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itidng.pojo.Category;
import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("category")
@Slf4j
public class CategoryController {

    private String key;
    /**
     * 类别方法自动装配
     */
    @Autowired
    private CategoryService mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 类别列表展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("page")
    public R<Object> categoryList(int page, int pageSize) {
        //设置redis的key值
        key = "category" + page + pageSize;
        //redis缓存减轻数据库访问次数
        if (redisTemplate.opsForValue().get(key) == null) {
            Page<Category> categoryPage = new Page<>(page, pageSize);
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper();
            wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
            mapper.page(categoryPage, wrapper);
            PageList pageList = new PageList();
            pageList.setPages(categoryPage.getPages());
            pageList.setRecords(categoryPage.getRecords());
            pageList.setTotal(categoryPage.getTotal());
            redisTemplate.opsForValue().set(key, pageList, 30, TimeUnit.MINUTES);
            return new R<>(1, pageList, "查询成功");
        }
        PageList pageList = (PageList) redisTemplate.opsForValue().get(key);
        log.info("redis中取出数据:---" + pageList);
        return new R<>(1, pageList, "查询成功");

    }


    /**
     * 新增菜品、套餐分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addCategory(@RequestBody Category category) {
        //判断是否已经存在该分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName());
        Category one = mapper.getOne(wrapper);
        if (one != null) {
            return new R<>(0, null, "该分类已存在，无需重复添加");
        }
        mapper.save(category);
        //数据库有变动则需要重新刷新缓存
        redisTemplate.delete(key);
        return new R<>(1, null, "添加成功");
    }


    /**
     * 修改菜品、套餐分类
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category) {
        mapper.updateById(category);
        redisTemplate.delete(key);
        return new R<>(1, null, "修改成功");
    }

    /**
     * 删除菜品、套餐分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> deleteCategory(@PathParam("ids") Long ids) {
        mapper.removeById(ids);
        redisTemplate.delete(key);
        return new R<>(1, null, "删除成功");
    }

    /**
     * 菜品添加时的菜品分类列表展示
     *
     * @param type
     * @return
     */
    @GetMapping("list")
    public R<List<Category>> getCategoryName(@PathParam("type") Integer type) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null, Category::getType, type);
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = mapper.list(wrapper);
        return new R<>(1, list, "分类查询成功");
    }

}
