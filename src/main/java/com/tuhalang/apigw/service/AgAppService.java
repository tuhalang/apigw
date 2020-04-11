package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgApp;

public interface AgAppService {
    AgApp findByAppName(String appName) throws Exception;
}
