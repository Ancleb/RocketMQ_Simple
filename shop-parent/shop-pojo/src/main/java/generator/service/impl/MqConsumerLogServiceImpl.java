package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.MqConsumerLogService;
import com.yyl.pojo.MqConsumerLog;
import generator.mapper.MqConsumerLogMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class MqConsumerLogServiceImpl extends ServiceImpl<MqConsumerLogMapper, MqConsumerLog>
    implements MqConsumerLogService {

}




