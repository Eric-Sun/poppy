package com.j13.poppy;

public class ErrorResponse {

    public ErrorResponse() {
    }

    public ErrorResponse(int code) {
        this.code = code;
    }

    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
