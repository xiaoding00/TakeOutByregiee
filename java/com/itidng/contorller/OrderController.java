package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itidng.pojo.Orders;
import com.itidng.result.CurrentId;
import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrdersService mapper;

    /**
     * 客户端订单提交
     *
     * @param orders
     * @return
     */
    @PostMapping("submit")
    public R<String> submitOrder(@RequestBody Orders orders) {
        mapper.submitOrder(orders);
        return new R<>(1, null, "订单提交完成");
    }


    /**
     * 个人中心展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("userPage")
    public R<PageList> getUSer(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Page<Orders> userPage = new Page<>(page, pageSize);
        Long currentId = CurrentId.getCurrentId();
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, currentId);
        mapper.page(userPage, wrapper);
        PageList pageList = new PageList();
        pageList.setPages(userPage.getPages());
        pageList.setRecords(userPage.getRecords());
        pageList.setTotal(userPage.getTotal());
        return new R<>(1, pageList, "用户信息展示");
    }

//  再来一单
//    请求 URL:
//    http://localhost/order/again
//    请求方法:
//    POST

    @PostMapping("again")
    public R<String> againOrder(@RequestBody Map<String,Long> map){
        //用户订单Id
        Long id = map.get("id");
        return null;
    }

    /**
     * 员工端，订单展示
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("page")
    public R<PageList> orderList(int page, int pageSize, String number, String beginTime, String endTime) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        if (number != null) {
            wrapper.eq(Orders::getNumber, number);
        }
        if (beginTime != null && endTime != null) {
            log.info("时间信息为：" + beginTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime begin = LocalDateTime.parse(beginTime, formatter);
            LocalDateTime end = LocalDateTime.parse(endTime, formatter);
            wrapper.between(Orders::getOrderTime, begin, end);
        }
        mapper.page(pageInfo, wrapper);
        PageList pageList = new PageList();
        pageList.setTotal(pageInfo.getTotal());
        pageList.setRecords(pageInfo.getRecords());
        pageList.setPages(pageInfo.getPages());

        return new R<>(1, pageList, "订单显示成功");

    }

    /**
     * 员工端订单状态修改
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        Long id = orders.getId();
        Integer status = orders.getStatus();
        Orders order = mapper.getById(id);
        order.setStatus(status);
        mapper.updateById(order);
        return new R<>(1, null, "订单状态修改成功");
    }
}
