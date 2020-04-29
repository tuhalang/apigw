package com.tuhalang.apigw.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RequestTest implements Serializable {
    private String username;
    private String apiName;
    private String apiKey;
    private LinkedHashMap<String, Object> wsRequest;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public LinkedHashMap<String, Object> getWsRequest() {
        return wsRequest;
    }

    public void setWsRequest(LinkedHashMap<String, Object> wsRequest) {
        this.wsRequest = wsRequest;
    }
}
