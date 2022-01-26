package com.yyl.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.CouponService;
import com.yyl.coupon.mapper.CouponMapper;
import com.yyl.pojo.Coupon;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon>
    implements CouponService {

}




