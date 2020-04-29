package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgApi;

public interface AgApiService {
    AgApi findByApiName(String apiName) throws Exception;
}
