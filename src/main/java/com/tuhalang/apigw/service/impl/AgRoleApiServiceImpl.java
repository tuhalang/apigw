package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgRoleApiDao;
import com.tuhalang.apigw.domain.AgRoleApi;
import com.tuhalang.apigw.service.AgRoleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgRoleApiServiceImpl implements AgRoleApiService {

    @Autowired
    private AgRoleApiDao agRoleApiDao;

    @Override
    public void save(AgRoleApi agRoleApi) throws Exception {
        agRoleApiDao.save(agRoleApi);
    }
}
