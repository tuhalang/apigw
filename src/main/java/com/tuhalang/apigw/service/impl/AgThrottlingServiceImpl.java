package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgThrottlingDao;
import com.tuhalang.apigw.domain.AgThrottling;
import com.tuhalang.apigw.service.AgThrottlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgThrottlingServiceImpl implements AgThrottlingService {

    @Autowired
    private AgThrottlingDao agThrottlingDao;

    @Override
    public AgThrottling findByThrottlingName(String throttlingName) throws Exception {
        return agThrottlingDao.findByThrottlingName(throttlingName);
    }
}
