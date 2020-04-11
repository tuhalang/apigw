package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgServiceDao;
import com.tuhalang.apigw.domain.AgRole;
import com.tuhalang.apigw.domain.AgService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgServiceDaoImpl implements AgServiceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AgService findByServiceName(String serviceName) {
        String sql = "select a from AgService a where a.serviceName=:serviceName";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgService.class);
        query.setParameter("serviceName", serviceName);
        List<AgService> list = query.list();
        if(list.size()>0)
            return list.get(0);
        return null;
    }
}
