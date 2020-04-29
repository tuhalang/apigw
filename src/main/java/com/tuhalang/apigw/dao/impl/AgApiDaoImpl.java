package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgApiDao;
import com.tuhalang.apigw.domain.AgApi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgApiDaoImpl implements AgApiDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public AgApi findByApiName(String apiName) throws Exception {
        String sql = "SELECT distinct a FROM AgApi a JOIN FETCH a.endpoints e WHERE a.apiName = :apiName and e.status=:status ORDER BY e.lastUsed";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgApi.class);
        query.setParameter("apiName", apiName);
        query.setParameter("status", "00");
        List<AgApi> list = query.list();
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
