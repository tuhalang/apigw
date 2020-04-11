package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgThrottling;

public interface AgThrottlingService {
    AgThrottling findByThrottlingName(String throttlingName) throws Exception;
}
