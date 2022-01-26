package com.yyl.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.PayService;
import com.yyl.pay.mapper.PayMapper;
import com.yyl.pojo.Pay;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService(interfaceClass = PayServiceImpl.class)
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay>
    implements PayService {

}




