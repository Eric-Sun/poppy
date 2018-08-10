package com.j13.poppy.exceptions;

public class RequestFatalException extends RuntimeException {

    public RequestFatalException(String msg, Throwable t) {
        super(msg, t);
    }

    public RequestFatalException(String msg) {
        super(msg);
    }


}
