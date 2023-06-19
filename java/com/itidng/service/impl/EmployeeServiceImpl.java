package com.itidng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.EmployeeMapper;
import com.itidng.pojo.Employee;
import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public PageList employeeList(int page,int pageSize,String name) {
        Page<Employee> pageInfo = new Page<Employee>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();

    /*     if (name != null){
            wrapper.like(Employee::getName,name);
            }
            优化
    */

        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        wrapper.orderByDesc(Employee::getUpdateTime);
       page(pageInfo, wrapper);
        List<Employee> records = pageInfo.getRecords();
        long total = pageInfo.getTotal();
        PageList employeeList = new PageList();
        employeeList.setRecords(records);
        employeeList.setPages(total);
        return employeeList;
    }
}
