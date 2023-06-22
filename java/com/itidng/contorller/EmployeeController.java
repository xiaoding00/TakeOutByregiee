package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.itidng.result.PageList;
import com.itidng.result.R;
import com.itidng.pojo.Employee;
import com.itidng.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    //用于存放当前Redis_key值
    private String key;

    /**
     * EmployeeService的自动装配
     */
    @Autowired
    private EmployeeService mapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 员工登录认证
     *
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("进入登入页面");
        String password = employee.getPassword();
        //对密码进行加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //条件判断
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        //获取符合条件的员工
        Employee emp = mapper.getOne(wrapper);
        //判断员工是否存在
        if (emp == null) {
            return new R<Employee>(0, emp, "用户未注册");
        }
        //判断员工登录密码是否正确
        if (!password.equals(emp.getPassword())) {
            return new R<Employee>(0, emp, "密码错误");
        }
        //判断员工是否被禁用
        if (emp.getStatus() == 0) {
            return new R<Employee>(0, emp, "员工已被禁用");
        }
        //以上全部通过则登录成功,并将员工ID放入session中
        request.getSession().setAttribute("employee", emp.getId());
        return new R<Employee>(1, emp, "登录成功");
    }

    /**
     * 展示员工列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    //http://localhost/employee/page?page=1&pageSize=10
    @GetMapping("page")
    public R<Object> getEmployeeList(int page, int pageSize, String name, HttpServletRequest request) {

        //设置redis的key值
        key = page + pageSize + name;
        //redis缓存减轻数据库访问次数
        if (redisTemplate.opsForValue().get(key) == null) {
            PageList employeeList = mapper.employeeList(page, pageSize, name);
            redisTemplate.opsForValue().set(key, employeeList, 30, TimeUnit.MINUTES);
            return new R<Object>(1, employeeList, "员工展示");
        }

        //如果存在则从redis中获取数据
        PageList employeeList = (PageList) redisTemplate.opsForValue().get(key);
        log.info("redis中取出数据:---" + employeeList);
        return new R<Object>(1, employeeList, "员工展示");
    }

    /**
     * 员工退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("logout")
    public R<Employee> logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return new R<>(1, null, "退出成功");
    }

    /**
     * 增添员工功能
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<Employee> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("注册新员工");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        mapper.save(employee);
        //数据修改，删除redis缓存中的数据，进而刷新新数据。
        redisTemplate.delete(key);
        return new R<>(1, null, "添加成功");
    }

    /**
     * 员工修改功能
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<Employee> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("修改成员为：----" + employee);
        mapper.updateById(employee);
        //数据修改，删除redis缓存中的数据，进而刷新新数据。
        redisTemplate.delete(key);
        return new R<>(1, null, "修改成功");
    }

    /**
     * 员工查询功能BY_ID，提供给员工修改功能
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> updateEmployee(HttpServletRequest request, @PathVariable Long id) {
        Employee employee = mapper.getById(id);
        return new R<>(1, employee, "查询成功");
    }


}


