package com.itidng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.CategoryMapper;
import com.itidng.pojo.Category;
import com.itidng.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService  {
}
