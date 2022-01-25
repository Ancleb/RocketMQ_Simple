package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.pojo.Goods;
import generator.service.GoodsService;
import generator.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




