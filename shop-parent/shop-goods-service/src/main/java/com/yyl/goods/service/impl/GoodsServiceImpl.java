package com.yyl.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyl.api.GoodsService;
import com.yyl.goods.mapper.GoodsMapper;
import com.yyl.pojo.Goods;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 */
@DubboService
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService {

}




