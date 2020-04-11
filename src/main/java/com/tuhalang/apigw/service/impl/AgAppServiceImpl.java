package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgAppDao;
import com.tuhalang.apigw.domain.AgApp;
import com.tuhalang.apigw.service.AgAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgAppServiceImpl implements AgAppService {

    @Autowired
    private AgAppDao agAppDao;

    @Override
    public AgApp findByAppName(String appName) throws Exception {
        return agAppDao.findByAppName(appName);
    }
}
