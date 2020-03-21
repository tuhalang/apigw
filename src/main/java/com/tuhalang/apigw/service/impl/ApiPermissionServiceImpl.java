package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.ApiPermissionDao;
import com.tuhalang.apigw.domain.ApiPermission;
import com.tuhalang.apigw.service.ApiPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiPermissionServiceImpl implements ApiPermissionService {

    @Autowired
    private ApiPermissionDao apiPermissionDao;

    @Override
    public ApiPermission getPermission(String username, String apiName) throws Exception {
        return apiPermissionDao.getPermission(username, apiName);
    }
}
