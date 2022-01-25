package com.yyl.controller;

import com.yyl.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者Controller
 *
 * @author yyl
 * 2022/1/25 11:48
 */
@RestController
@RequestMapping("user")
public class UserController {
    //引用dubbo服务
    @DubboReference
    private UserService userService;

    @GetMapping("hello")
    public String hellO(String name){
        return userService.sayHello(name);
    }
}
