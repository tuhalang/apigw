package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.ApiResource;

public interface ApiResourceDao {
    ApiResource findbyApiName(String apiName) throws Exception;
}
