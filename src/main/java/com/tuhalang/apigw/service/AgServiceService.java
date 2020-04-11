package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgService;

public interface AgServiceService {
    AgService findByServiceName(String serviceName) throws Exception;
}
