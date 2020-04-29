package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgEndpoint;

public interface AgEndpointService {
    void updateTimeAccess(AgEndpoint agEndpoint) throws Exception;
    void healthyCheck() throws Exception;
}
