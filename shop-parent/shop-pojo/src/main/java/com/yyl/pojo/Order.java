package com.yyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName trade_order
 */
@TableName(value ="trade_order")
@Data
public class Order implements Serializable {
    /**
     * 订单ID
     * ASSIGN_ID:雪花算法
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long orderId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单状态 0未确认 1已确认 2已取消 3无效 4退款
     */
    private Integer orderStatus;

    /**
     * 支付状态 0未支付 1支付中 2已支付
     */
    private Integer payStatus;

    /**
     * 发货状态 0未发货 1已发货 2已收货
     */
    private Integer shippingStatus;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不存在")
    private Long goodsId;

    /**
     * 商品数量
     */
    private Integer goodsNumber;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品总价
     */
    private Integer goodsAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 订单价格
     */
    private BigDecimal orderAmount;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 优惠券
     */
    private BigDecimal couponPaid;

    /**
     * 已付金额
     */
    private BigDecimal moneyPaid;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 创建时间
     */
    private Date addTime;

    /**
     * 订单确认时间
     */
    private Date confirmTime;

    /**
     * 支付时间
     */
    private Date payTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}