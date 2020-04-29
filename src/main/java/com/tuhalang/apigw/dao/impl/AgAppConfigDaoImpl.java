package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgAppConfigDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgAppConfigDaoImpl implements AgAppConfigDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean checkDatabase(){
        try {
            String sql = "SELECT 1";
            Session session = sessionFactory.getCurrentSession();
            Query query = session.createSQLQuery(sql);
            query.list();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
