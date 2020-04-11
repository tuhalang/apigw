package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgAppDao;
import com.tuhalang.apigw.domain.AgApp;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgAppDaoImpl implements AgAppDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AgApp findByAppName(String appName) throws Exception{
        String sql = "select a from AgApp a where a.appName=:appName";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgApp.class);
        query.setParameter("appName", appName);
        List<AgApp> list = query.list();
        if(list.size()>0)
            return list.get(0);
        return null;
    }
}
