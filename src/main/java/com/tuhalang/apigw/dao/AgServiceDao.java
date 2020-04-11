package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgService;

public interface AgServiceDao {
    AgService findByServiceName(String serviceName) throws Exception;
}
