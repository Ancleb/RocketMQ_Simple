package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.pojo.Pay;
import generator.service.PayService;
import generator.mapper.PayMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay>
    implements PayService{

}




