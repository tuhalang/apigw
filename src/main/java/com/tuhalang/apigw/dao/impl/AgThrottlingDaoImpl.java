package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgThrottlingDao;
import com.tuhalang.apigw.domain.AgThrottling;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgThrottlingDaoImpl implements AgThrottlingDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AgThrottling findByThrottlingName(String throttlingName) throws Exception {
        String sql = "select a from AgThrottling a where a.throttlingName=:throttlingName";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgThrottling.class);
        query.setParameter("throttlingName", throttlingName);
        List<AgThrottling> list = query.list();
        if(list.size()>0)
            return list.get(0);
        return null;
    }
}
