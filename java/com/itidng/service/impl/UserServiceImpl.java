package com.itidng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.UserMapper;
import com.itidng.pojo.User;
import com.itidng.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
