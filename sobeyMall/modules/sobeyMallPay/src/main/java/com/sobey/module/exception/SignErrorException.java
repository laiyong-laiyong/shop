package com.sobey.module.exception;

/**
 * @author WCY
 * @createTime 2020/7/13 11:47
 * 用于校验需要签名的接口，如代金券与充值余额等
 */
public class SignErrorException extends Exception {

    public SignErrorException() {
    }

    public SignErrorException(String message) {
        super(message);
    }

    public SignErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignErrorException(Throwable cause) {
        super(cause);
    }

    protected SignErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
