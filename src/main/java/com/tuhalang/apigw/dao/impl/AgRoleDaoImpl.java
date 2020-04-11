package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgRoleDao;
import com.tuhalang.apigw.domain.AgRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgRoleDaoImpl implements AgRoleDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AgRole findByRoleName(String roleName) {
        String sql = "select a from AgRole a where a.roleName=:roleName";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgRole.class);
        query.setParameter("roleName", roleName);
        List<AgRole> list = query.list();
        if(list.size()>0)
            return list.get(0);
        return null;
    }
}
