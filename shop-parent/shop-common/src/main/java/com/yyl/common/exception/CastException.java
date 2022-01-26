package com.yyl.common.exception;

import com.yyl.common.constant.ShopCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yyl
 * 2022/1/25 17:23
 */
@Slf4j
public class CastException{
    public static BizException cast(ShopCode shopCode) {
        log.error(shopCode.toString());
        return new BizException(shopCode.toString());
    }
}
