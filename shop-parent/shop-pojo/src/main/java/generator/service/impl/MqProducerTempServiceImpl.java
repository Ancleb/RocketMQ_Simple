package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.MqProducerTempService;
import com.yyl.pojo.MqProducerTemp;
import generator.mapper.MqProducerTempMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class MqProducerTempServiceImpl extends ServiceImpl<MqProducerTempMapper, MqProducerTemp>
    implements MqProducerTempService {

}




