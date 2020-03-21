package com.tuhalang.apigw.bean;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResponseBean implements Serializable {
    private String errorCode;
    private String message;
    private String description;
    private Object wsResponse;
    private String userMessage;

    public ResponseBean() {
    }

    public ResponseBean(String errorCode, String message, String description, Object wsResponse) {
        this.errorCode = errorCode;
        this.message = message;
        this.description = description;
        this.wsResponse = wsResponse;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getWsResponse() {
        return wsResponse;
    }

    public void setWsResponse(Object wsResponse) {
        this.wsResponse = wsResponse;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
