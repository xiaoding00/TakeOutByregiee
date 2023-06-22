package com.itidng.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itidng.pojo.Employee;
import com.itidng.result.PageList;
import com.itidng.result.R;


public interface EmployeeService extends IService<Employee> {
     PageList employeeList(int page,int pageSize,String name);
}
