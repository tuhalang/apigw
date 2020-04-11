package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgApp;

public interface AgAppDao {
    AgApp findByAppName(String appName) throws Exception;
}
