package com.j13.poppy.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CommonException extends RuntimeException {
    private static Logger LOG = LoggerFactory.getLogger(CommonException.class);
    private int errorCode = 0;
    private String errorMessage = "";

    public CommonException(int errorCode) {
        this.errorCode = errorCode;
    }

    public CommonException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CommonException(int errorCode,Throwable t) {
        super(t);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}