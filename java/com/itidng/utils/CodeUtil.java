package com.itidng.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import java.util.Random;


@Component
public class CodeUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${mail.sender}")
    private String sender;
    @Value("${mail.text}")
    private String text;

    //标题
    @Value("${mail.title}")
    private String title;

    public String sendCode(String getter) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, 4);
        message.setText("[坤坤外卖] 验证码: " + capstr + text);
        message.setTo(getter);
        message.setFrom(sender + "(坤坤外卖)");
        javaMailSender.send(message);
        return capstr;
    }

}
