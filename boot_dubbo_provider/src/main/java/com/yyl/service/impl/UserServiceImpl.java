package com.yyl.service.impl;

import com.yyl.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * Dubbo服务提供者
 *
 * @author yyl
 * 2022/1/25 9:45
 */
@Service
@DubboService(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String name) {
        return "Hello:" + name;
    }
}
