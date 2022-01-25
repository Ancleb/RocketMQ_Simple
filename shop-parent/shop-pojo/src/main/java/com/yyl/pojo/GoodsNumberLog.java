package com.yyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName trade_goods_number_log
 */
@TableName(value ="trade_goods_number_log")
@Data
public class GoodsNumberLog implements Serializable {
    /**
     * 商品ID
     */
    @TableId
    private Long goodsId;

    /**
     * 订单ID
     */
    @TableId
    private Long orderId;

    /**
     * 库存数量
     */
    private Integer goodsNumber;

    /**
     * 
     */
    private Date logTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}