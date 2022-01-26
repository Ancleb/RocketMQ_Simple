package com.yyl.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.UserMoneyLogService;
import com.yyl.pojo.UserMoneyLog;
import com.yyl.user.mapper.UserMoneyLogMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class UserMoneyLogServiceImpl extends ServiceImpl<UserMoneyLogMapper, UserMoneyLog>
    implements UserMoneyLogService {

}




