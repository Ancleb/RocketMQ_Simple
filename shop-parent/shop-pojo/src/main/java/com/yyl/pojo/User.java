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
 * @TableName trade_user
 */
@TableName(value ="trade_user")
@Data
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 积分
     */
    private Integer userScore;

    /**
     * 注册时间
     */
    private Date userRegTime;

    /**
     * 用户余额
     */
    private Integer userMoney;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}