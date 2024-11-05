package org.sunbong.allmart_api.common.exception;

public class TaskException extends RuntimeException {

    private int code;
    private String message;

    TaskException(String message, int status) {
        this.message = message;
        this.code = status;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}