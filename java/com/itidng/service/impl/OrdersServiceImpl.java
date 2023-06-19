package com.itidng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.OrdersMapper;
import com.itidng.pojo.*;
import com.itidng.result.CurrentId;
import com.itidng.result.PageList;
import com.itidng.service.*;
import org.apache.ibatis.javassist.bytecode.analysis.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;

    @Transactional
    public void submitOrder(Orders orders) {
        //查询当前用户购物车数据
        Long userId = CurrentId.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartWrapper);
        //向订单表中插入数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        //创建订单号
        long orderId = IdWorker.getId();
        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        //设置订单地址
        orders.setUserId(userId);
        //设置为待付款
        orders.setStatus(1);
        //设置为微信支付 和 支付时间 下单时间
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayMethod(1);
        orders.setCheckoutTime(LocalDateTime.now());
        //设置为待派送
        orders.setStatus(2);
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        User user = userService.getById(userId);
        //客户端中似乎没有设置姓名的地点？
        orders.setUserName(user.getPhone());

        //原子类整数 保证线程安全
        AtomicInteger amount = new AtomicInteger(0);
        //计算订单总额并购物车数据放入订单明细中
        List<OrderDetail> detailList = shoppingCartList.stream().map((item) -> {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(item, detail, "userId", "createTime");
            detail.setOrderId(orderId);
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return detail;
        }).collect(Collectors.toList());

        orderDetailService.saveBatch(detailList);
        orders.setAmount(new BigDecimal(amount.get()));
        this.save(orders);
        //清空购物车数据
        shoppingCartService.remove(shoppingCartWrapper);

    }


}
