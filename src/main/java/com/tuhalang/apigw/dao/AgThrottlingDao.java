package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgThrottling;

public interface AgThrottlingDao {

    AgThrottling findByThrottlingName(String throttlingName) throws Exception;
}
