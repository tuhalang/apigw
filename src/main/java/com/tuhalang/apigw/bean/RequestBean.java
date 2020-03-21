package com.tuhalang.apigw.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RequestBean implements Serializable {
    private String session;
    private String username;
    private String token;
    private String apiName;
    private String wsKey;
    private String method;
    private LinkedHashMap<String, Object> wsRequest;


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getWsKey() {
        return wsKey;
    }

    public void setWsKey(String wsKey) {
        this.wsKey = wsKey;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LinkedHashMap<String, Object> getWsRequest() {
        return wsRequest;
    }

    public void setWsRequest(LinkedHashMap<String, Object> wsRequest) {
        this.wsRequest = wsRequest;
    }
}
