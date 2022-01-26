package com.yyl.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.*;
import com.yyl.common.constant.ShopCode;
import com.yyl.common.exception.BizException;
import com.yyl.common.exception.CastException;
import com.yyl.common.utils.IDWorker;
import com.yyl.common.utils.R;
import com.yyl.order.mapper.OrderMapper;
import com.yyl.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.zookeeper.Op;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author yyl
 * 2022/1/26 9:54
 */
@Slf4j
@DubboService
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @DubboReference
    private GoodsService goodsService;
    @DubboReference
    private UserService userService;
    @DubboReference
    private CouponService couponService;
    @DubboReference
    private GoodsNumberLogService goodsNumberLogService;
    @DubboReference
    private UserMoneyLogService userMoneyLogService;

    @Override
    public R confirmOrder(Order order) {
        // 1. 校验订单
        checkOrder(order);
        try {
            // 2. 生成预订单
            Long orderId = savePreOrder(order);
            // 3. 扣减库存
            reduceGoodsNum(order);
            // 4. 扣减消费劵
            updateCouponStatus(order);
            // 5. 使用余额
            reduceMoneyPaid(order);
            // 6. 确认订单
            // 7. 返回成功状态

        }catch (Exception e){
            // 8. 确认订单失败，发送消息
            // 9. 返回失败状态
        }
        return null;
    }

    /**
     * 扣减余额
     */
    private void reduceMoneyPaid(Order order) {
       if (order.getMoneyPaid() != null && order.getMoneyPaid().compareTo(BigDecimal.ZERO) > 0){
           UserMoneyLog userMoneyLog = new UserMoneyLog(order.getUserId(), order.getOrderId(), ShopCode.SHOP_USER_MONEY_PAID.getCode(), order.getMoneyPaid(), new Date());
           userMoneyLogService.save(userMoneyLog);
       }
    }

    /**
     * 更新优惠卷
     */
    private void updateCouponStatus(Order order) {
        Coupon coupon = couponService.getById(order.getCouponId());
        coupon.setOrderId(order.getOrderId());
        coupon.setIsUsed(ShopCode.SHOP_COUPON_ISUSED.getCode());
        coupon.setUsedTime(new Date());
        //更新优惠卷状态
        couponService.lambdaUpdate().update(coupon);
    }

    /**
     * 扣减库存
     */
    private void reduceGoodsNum(Order order) {
        Goods goods = goodsService.getById(order.getGoodsId());
        if (goods.getGoodsNumber() < order.getGoodsNumber()){
            throw CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        goodsService.lambdaUpdate().eq(Goods::getGoodsId, order.getGoodsId()).set(Goods::getGoodsNumber, goods.getGoodsNumber() - order.getGoodsNumber()).update();
        GoodsNumberLog goodsNumberLog = new GoodsNumberLog(order.getGoodsId(), order.getOrderId(), -order.getGoodsNumber(), new Date());
        goodsNumberLogService.save(goodsNumberLog);
    }

    /**
     * 生成预订单
     */
    private Long savePreOrder(Order order) {
        // 设置订单状态不可见
        order.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        // 设置订单ID // type=IdType.ASSIGN_ID
        // order.setOrderId(new IDWorker(2, 3).nextId());
        // 核算订单运费
        BigDecimal shippingFree = Optional.ofNullable(calculateShippingFee(order.getOrderAmount()))
                .map(shippingFee -> shippingFee.compareTo(BigDecimal.ZERO) == 0 ? shippingFee : null)
                .orElseThrow(() -> CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID));
        //核算订单总金额是否合法 商品金额+运费 == 订单总额
        BigDecimal orderAmount = order.getGoodsPrice().multiply(new BigDecimal(order.getGoodsNumber()));
        orderAmount = orderAmount.add(shippingFree);
        if (orderAmount.compareTo(order.getOrderAmount()) == 0) {
            throw CastException.cast(ShopCode.SHOP_ORDERAMOUNT_INVALID);
        }
        // 判断用户是否使用余额
        BigDecimal moneyPaid = order.getMoneyPaid();
        if (moneyPaid != null){
            int compare = moneyPaid.compareTo(BigDecimal.ZERO);
            if (compare < 0) {
                throw CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            } else if (compare > 0) {
                Integer userMoney = userService.getById(order.getUserId()).getUserMoney();
                if (moneyPaid.compareTo(new BigDecimal(userMoney)) > 0) {
                    throw CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALID);
                }
            }
        }else {
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        // 判断用户是否使用优惠卷
        Long couponId = order.getCouponId();
        if (couponId == null){
            Coupon coupon = couponService.getById(couponId);
            // 优惠卷是否在数据库中存在
            if (coupon == null) throw CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
            // 优惠卷是否已被使用
            if (coupon.getIsUsed().equals(ShopCode.SHOP_COUPON_ISUSED.getCode())) throw CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
        }else {
            order.setCouponId(0L);
        }
        // 核算订单支付金额  订单总金额-余额-优惠卷金额
        BigDecimal payAmount = order.getOrderAmount().subtract(order.getMoneyPaid()).subtract(order.getCouponPaid());
        order.setPayAmount(payAmount);
        // 设置下单时间
        order.setAddTime(new Date());
        // 持久化预订单
        this.save(order);
        return order.getOrderId();
    }

    /**
     * 核算运费
     * 订单总额>100免运费
     */
    private BigDecimal calculateShippingFee(BigDecimal orderAmount) {
        return Optional.ofNullable(orderAmount).orElseThrow(() -> CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID))
                .compareTo(new BigDecimal(100)) > 0 ? BigDecimal.ZERO : BigDecimal.TEN;
    }

    /**
     * 校验订单
     */
    private void checkOrder(Order order) {
        try {
            //1. 订单是否存在
            Optional.ofNullable(order).orElseThrow(() -> CastException.cast(ShopCode.SHOP_ORDER_INVALID));
            //2. 商品是否存在
            Goods goods = Optional.ofNullable(goodsService.getById(order.getGoodsId())).orElseThrow(() -> CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST));
            //3. 校验下单用户是否合法
            Optional.ofNullable(userService.getById(order.getUserId())).orElseThrow(() -> CastException.cast(ShopCode.SHOP_USER_NO_EXIST));
            //4. 订单金额是否合法
            Optional.ofNullable(order.getGoodsPrice().compareTo(goods.getGoodsPrice()) != 0 ? true : null).orElseThrow(() -> CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID));
            //5. 订单数量是否合法
            Optional.ofNullable(order.getGoodsAmount() > goods.getGoodsNumber() ? true : null).orElseThrow(() -> CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH));
        } catch (Throwable e){
            throw new BizException(e.getMessage());
        }
        log.info("校验订单通过");
    }
}
