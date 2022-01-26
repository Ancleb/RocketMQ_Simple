package com.yyl.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.UserService;
import com.yyl.pojo.User;
import com.yyl.user.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




