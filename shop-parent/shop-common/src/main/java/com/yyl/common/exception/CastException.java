package com.yyl.common.exception;

import com.yyl.common.constant.ShopCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yyl
 * 2022/1/25 17:23
 */
@Slf4j
public class CastException{
    public static void cast(ShopCode shopCode) {
        log.error(shopCode.toString());
        throw new BizException(shopCode.toString());
    }
}
