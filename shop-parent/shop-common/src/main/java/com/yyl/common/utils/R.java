package com.yyl.common.utils;

/**
 * @author yyl
 * 2022/1/25 17:35
 */
public class R {
    private Boolean success;
    private String message;

    public R() {
    }

    public R(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
