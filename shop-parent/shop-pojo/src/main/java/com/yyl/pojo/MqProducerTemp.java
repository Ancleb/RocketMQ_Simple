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
 * @TableName trade_mq_producer_temp
 */
@TableName(value ="trade_mq_producer_temp")
@Data
public class MqProducerTemp implements Serializable {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String groupName;

    /**
     * 
     */
    private String msgTopic;

    /**
     * 
     */
    private String msgTag;

    /**
     * 
     */
    private String msgKey;

    /**
     * 
     */
    private String msgBody;

    /**
     * 0:未处理;1:已经处理
     */
    private Integer msgStatus;

    /**
     * 
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}