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
 * @TableName trade_goods
 */
@TableName(value ="trade_goods")
@Data
public class Goods implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品库存
     */
    private Integer goodsNumber;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 添加时间
     */
    private Date addTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}