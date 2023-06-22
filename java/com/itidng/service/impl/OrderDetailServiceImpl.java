package com.itidng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.OrderDetailMapper;
import com.itidng.pojo.OrderDetail;
import com.itidng.service.OrderDetailService;

import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
