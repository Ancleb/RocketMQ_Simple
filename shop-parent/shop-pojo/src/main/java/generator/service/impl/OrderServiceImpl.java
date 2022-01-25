package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.pojo.Order;
import generator.service.OrderService;
import generator.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




