package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.ApiResourceDao;
import com.tuhalang.apigw.domain.ApiResource;
import com.tuhalang.apigw.service.ApiResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiResourceServiceImpl implements ApiResourceService {

    @Autowired
    private ApiResourceDao apiResourceDao;

    @Override
    public ApiResource findbyApiName(String apiName) throws Exception {
        return apiResourceDao.findbyApiName(apiName);
    }
}
