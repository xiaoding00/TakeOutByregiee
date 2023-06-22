package com.itidng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itidng.pojo.Orders;
import com.itidng.result.PageList;

public interface OrdersService extends IService<Orders> {

    void submitOrder(Orders orders);

}
