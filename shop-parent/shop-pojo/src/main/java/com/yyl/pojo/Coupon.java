package com.yyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName trade_coupon
 */
@TableName(value ="trade_coupon")
@Data
public class Coupon implements Serializable {
    /**
     * 优惠券ID
     */
    @TableId
    private Long couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 是否使用 0未使用 1已使用
     */
    private Integer isUsed;

    /**
     * 使用时间
     */
    private Date usedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}