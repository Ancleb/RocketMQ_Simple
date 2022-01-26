package com.yyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName trade_user_money_log
 */
@TableName(value ="trade_user_money_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoneyLog implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 订单ID
     */
    @TableId
    private Long orderId;

    /**
     * 日志类型 1订单付款 2 订单退款
     */
    @TableId
    private Integer moneyLogType;

    /**
     * 
     */
    private BigDecimal useMoney;

    /**
     * 日志时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}