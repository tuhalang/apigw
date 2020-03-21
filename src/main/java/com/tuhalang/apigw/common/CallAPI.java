package com.tuhalang.apigw.common;

import java.util.LinkedHashMap;

public class CallAPI extends MyRunnable {

    private LinkedHashMap<String, Object> payload;
    private String endpoint;
    private String resource;
    private String apiKey;
    private String username;
    private String wsName;
    private int method;
    private long timeout;

    public CallAPI(LinkedHashMap<String, Object> payload, String endpoint, String resource, String apiKey,
                   String username, String wsName, int method, long timeout) {
        this.payload = payload;
        this.endpoint = endpoint;
        this.resource = resource;
        this.apiKey = apiKey;
        this.username = username;
        this.wsName = wsName;
        this.method = method;
        this.timeout = timeout;
    }

    @Override
    protected void execute() {
        this.resultObject = ApiUtils.callApi(this.payload, this.endpoint, this.resource, this.apiKey,
                this.username, this.wsName, this.method, this.timeout);
    }
}
