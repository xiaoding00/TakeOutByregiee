package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itidng.pojo.User;

import com.itidng.result.R;
import com.itidng.service.UserService;
import com.itidng.utils.CodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService mapper;
    @Autowired
    private CodeUtil codeUtil;

    @PostMapping("sendMsg")
    public R<String> sendCodeMsg(@RequestBody HashMap<String, String> map, HttpServletRequest request) {
        String phone = map.get("phone");
        String code = codeUtil.sendCode(phone);
        HttpSession session = request.getSession();
        //后面由redis替代
        session.setAttribute("code", code);
        return new R<>(1, null, "验证码已发送至您的邮箱中，注意查收");
    }


    /**
     * 用户登录
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("login")
    public R<User> userLogin(@RequestBody Map<String, String> map, HttpServletRequest request) {
        //手机号，这里由邮箱号替代
        String phone = map.get("phone");
        //验证码
        String code = map.get("code");
        //从session中获取邮箱所发送的验证码
        HttpSession session = request.getSession();
        String codeInfo = (String) session.getAttribute("code");
        //判断验证码是否为空
        if (code == null)
            return new R<>(0, null, "验证码为空");


        //判断验证码是否正确
        if (!codeInfo.equals(code))
            return new R<>(0, null, "验证码错误");

        //获取用户对象
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = mapper.getOne(wrapper);
        //如果为null则创建对象
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            mapper.save(user);
        }
        session.setAttribute("user", user);
        //登录成功后，将传入的验证码删除。
        request.getSession().removeAttribute("code");
        return new R<>(1, user, "登录成功");
    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new R<>(1, null, "您已退出该系统");
    }


}
