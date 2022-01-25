package com.yyl.common.exception;

/**
 * @author yyl
 * 2022/1/25 17:23
 */
public class BizException extends RuntimeException{
    public BizException() {
    }

    public BizException(String message) {
        super(message);
    }
}
