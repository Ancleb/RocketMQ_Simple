package com.yyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName trade_pay
 */
@TableName(value ="trade_pay")
@Data
public class Pay implements Serializable {
    /**
     * 支付编号
     */
    @TableId
    private Long payId;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 是否已支付 1否 2是
     */
    private Integer isPaid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}