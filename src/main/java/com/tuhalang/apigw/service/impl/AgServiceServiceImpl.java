package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgServiceDao;
import com.tuhalang.apigw.domain.AgService;
import com.tuhalang.apigw.service.AgServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgServiceServiceImpl implements AgServiceService {

    @Autowired
    private AgServiceDao agServiceDao;

    @Override
    public AgService findByServiceName(String serviceName) throws Exception {
        return agServiceDao.findByServiceName(serviceName);
    }
}
