package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.AgRoleDao;
import com.tuhalang.apigw.domain.AgRole;
import com.tuhalang.apigw.service.AgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgRoleServiceImpl implements AgRoleService {

    @Autowired
    private AgRoleDao agRoleDao;


    @Override
    public AgRole findByRoleName(String roleName) throws Exception {
        return agRoleDao.findByRoleName(roleName);
    }
}
