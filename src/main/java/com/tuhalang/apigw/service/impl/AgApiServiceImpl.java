package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgApiDao;
import com.tuhalang.apigw.domain.AgApi;
import com.tuhalang.apigw.service.AgApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgApiServiceImpl implements AgApiService {

    @Autowired
    private AgApiDao agApiDao;

    @Override
    public AgApi findByApiName(String apiName) throws Exception {
        return agApiDao.findByApiName(apiName);
    }
}
