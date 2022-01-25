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
 * @TableName trade_mq_consumer_log
 */
@TableName(value ="trade_mq_consumer_log")
@Data
public class MqConsumerLog implements Serializable {
    /**
     * 
     */
    @TableId
    private String groupName;

    /**
     * 
     */
    @TableId
    private String msgTag;

    /**
     * 
     */
    @TableId
    private String msgKey;

    /**
     * 
     */
    private String msgId;

    /**
     * 
     */
    private String msgBody;

    /**
     * 0:正在处理;1:处理成功;2:处理失败
     */
    private Integer consumerStatus;

    /**
     * 
     */
    private Integer consumerTimes;

    /**
     * 
     */
    private Date consumerTimestamp;

    /**
     * 
     */
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}