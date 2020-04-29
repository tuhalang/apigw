package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgApi;

public interface AgApiDao {
    AgApi findByApiName(String apiName) throws Exception;
}
