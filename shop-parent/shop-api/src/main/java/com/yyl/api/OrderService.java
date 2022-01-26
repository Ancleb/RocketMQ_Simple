package com.yyl.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyl.common.utils.R;
import com.yyl.pojo.Order;

/**
 * @author  yyl
 * 2022/1/26 9:51
 */
public interface OrderService extends IService<Order> {
    /**
     * 下订单
     */
    R confirmOrder(Order order);
}
