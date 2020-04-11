package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgRoleApiDao;
import com.tuhalang.apigw.domain.AgRoleApi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgRoleApiDaoImpl implements AgRoleApiDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(AgRoleApi agRoleApi) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.save(agRoleApi);
    }
}
