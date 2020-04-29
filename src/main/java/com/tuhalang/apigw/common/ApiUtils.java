package com.tuhalang.apigw.common;

import com.tuhalang.apigw.bean.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

public class ApiUtils {

    public static String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }

    public static String callHealthyCheck(String endpoint, String resource) {
        try {
            Client client = ClientBuilder.newBuilder()
                    .connectTimeout(60000, TimeUnit.MILLISECONDS)
                    .build();
            Response response = null;
            WebTarget webTarget = null;
            Invocation.Builder invocationBuilder = null;

            webTarget = client.target(endpoint).path(resource);
            invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
            invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
            response = invocationBuilder.get();
            if(response.getStatus() == 200)
                return response.readEntity(String.class);
            return "11";
        } catch (Exception e) {
            return "11";
        }
    }

    public static Map<String, Object> callApi(LinkedHashMap<String, Object> payload, String endpoint, String resource,
                                              String apiKey, String username, String apiName, int method, long timeout) {
        try {
            Client client = ClientBuilder.newBuilder()
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .build();

            Response response = null;
            WebTarget webTarget = null;
            Invocation.Builder invocationBuilder = null;

            switch (method) {
                case HttpMethod.GET:
                    webTarget = client.target(endpoint).path(resource);
                    if (payload != null && !payload.isEmpty()) {
                        for (String key : payload.keySet()) {
                            webTarget = webTarget.queryParam(key, payload.get(key).toString());
                        }
                    }
                    webTarget = webTarget.queryParam("apiKey", apiKey);
                    webTarget = webTarget.queryParam("apiName", apiName);
                    webTarget = webTarget.queryParam("username", username);
                    invocationBuilder = webTarget.request(APPLICATION_JSON_TYPE);
                    invocationBuilder.header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                    response = invocationBuilder.get();
                    break;
                case HttpMethod.DELETE:
                    webTarget = client.target(endpoint).path(resource);
                    if (payload != null && !payload.isEmpty()) {
                        for (String key : payload.keySet()) {
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
                    requestPost.put("apiName", apiName);
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
                    requestPut.put("apiName", apiName);
                    requestPut.put("username", username);
                    requestPut.put("wsRequest", payload);
                    response = invocationBuilder.put(Entity.entity(requestPut, APPLICATION_JSON_TYPE));
                    break;
            }

            Map<String, Object> result = null;
            if (response != null) {
                result = new HashMap<>();
                result.put("status", response.getStatus());
                if (response.hasEntity())
                    result.put("result", response.readEntity(String.class));
                else
                    result.put("result", null);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
