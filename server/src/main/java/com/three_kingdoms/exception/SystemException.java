package com.three_kingdoms.exception;

import com.three_kingdoms.controller.ResultCode;

public class SystemException extends RuntimeException{
    private ResultCode code;

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public SystemException(ResultCode code, String message) {
        super(message);
        this.code = code;
    }

    public SystemException(ResultCode code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
