package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.dao.UserRoleDao;
import com.tuhalang.apigw.domain.UserRole;
import com.tuhalang.apigw.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("user_role_service")
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    @Qualifier("user_role_dao")
    private UserRoleDao userRoleDao;

    @Override
    public void save(UserRole userRole) throws Exception{
        userRoleDao.save(userRole);
    }
}
