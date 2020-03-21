package com.tuhalang.apigw.common;

import com.tuhalang.apigw.bean.HttpMethod;
import com.tuhalang.apigw.bean.RequestBean;

import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class ApiUtils {

    public static Map<String, Object> callApi(LinkedHashMap<String, Object> payload, String endpoint, String resource,
                                        String apiKey, String username,String wsName, int method, long timeout){

        Client client = ClientBuilder.newBuilder()
                            .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                            .build();

        Response response = null;
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;

        switch (method){
            case HttpMethod.GET:
                webTarget = client.target(endpoint).path(resource);
                if(payload != null && !payload.isEmpty()){
                    for(String key : payload.keySet()){
                        webTarget = webTarget.queryParam(key, payload.get(key).toString());
                    }
                }
                webTarget = webTarget.queryParam("apiKey", apiKey);
                webTarget = webTarget.queryParam("wsName", wsName);
                webTarget = webTarget.queryParam("username", username);
                invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
                invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                response = invocationBuilder.get();
                break;
            case HttpMethod.DELETE:
                webTarget = client.target(endpoint).path(resource);
                if(payload != null && !payload.isEmpty()){
                    for(String key : payload.keySet()){
                        webTarget = webTarget.queryParam(key, payload.get(key).toString());
                    }
                }
                webTarget = webTarget.queryParam("apiKey", apiKey);
                invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
                invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                response = invocationBuilder.delete();
                break;
            case HttpMethod.POST:
                webTarget = client.target(endpoint).path(resource);
                invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
                invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                LinkedHashMap<String, Object> requestPost = new LinkedHashMap<>();
                requestPost.put("apiKey", apiKey);
                requestPost.put("wsName", wsName);
                requestPost.put("username", username);
                requestPost.put("wsRequest", payload);
                response = invocationBuilder.post(Entity.entity(requestPost, APPLICATION_JSON_TYPE));
                break;
            case HttpMethod.PUT:
                webTarget = client.target(endpoint).path(resource);
                invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
                invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                LinkedHashMap<String, Object> requestPut = new LinkedHashMap<>();
                requestPut.put("apiKey", apiKey);
                requestPut.put("wsName", wsName);
                requestPut.put("username", username);
                requestPut.put("wsRequest", payload);
                response = invocationBuilder.put(Entity.entity(requestPut, APPLICATION_JSON_TYPE));
                break;
        }

        Map<String, Object> result = null;
        if(response != null){
            result = new HashMap<>();
            result.put("status", response.getStatus());
            if(response.hasEntity())
                result.put("result", response.readEntity(String.class));
            else
                result.put("result", null);
        }
        return result;
    }
}
