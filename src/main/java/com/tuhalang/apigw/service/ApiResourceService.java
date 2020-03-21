package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.ApiResource;

public interface ApiResourceService {
    ApiResource findbyApiName(String apiName) throws Exception;
}
