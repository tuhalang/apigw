package com.tuhalang.apigw.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class RequestBean implements Serializable {
    private String session;
    private String username;
    private String token;
    private String apiName;
    private String apiKey;
    private String otp;
    private LinkedHashMap<String, Object> wsRequest;


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

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
