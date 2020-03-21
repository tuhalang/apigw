package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.UserRoleDao;
import com.tuhalang.apigw.domain.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("user_role_dao")
@Transactional(rollbackOn = Exception.class)
public class UserRoleDaoImpl implements UserRoleDao {

    @Autowired
    @Qualifier("apigw_session")
    private SessionFactory sessionFactory;

    @Override
    public void save(UserRole userRole) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.save(userRole);
    }
}
