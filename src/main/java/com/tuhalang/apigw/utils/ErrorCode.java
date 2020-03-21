package com.tuhalang.apigw.utils;

import java.io.Serializable;

public enum ErrorCode implements Serializable {
    ERROR_CODE_DEFAULT("1"),
    ERROR_CODE_OK("0");


    private String key;
    ErrorCode(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
