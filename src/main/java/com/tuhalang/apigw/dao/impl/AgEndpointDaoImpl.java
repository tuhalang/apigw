package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgEndpointDao;
import com.tuhalang.apigw.domain.AgEndpoint;
import org.hibernate.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.CallableStatement;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgEndpointDaoImpl implements AgEndpointDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void updateTimeAccess(AgEndpoint agEndpoint) throws Exception {
        agEndpoint.setLastUsed(new Date());
        Session session = sessionFactory.getCurrentSession();
        session.update(agEndpoint);
    }

    @Override
    public long count() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteriaCount = session.createCriteria(AgEndpoint.class);
        criteriaCount.setProjection(Projections.rowCount());
        Long count = (Long) criteriaCount.uniqueResult();
        return count;
    }

    @Override
    public List<AgEndpoint> find(int start, int size) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(AgEndpoint.class);
        criteria.setFirstResult(start);
        criteria.setMaxResults(size);
        List<AgEndpoint> list = criteria.list();
        return list;
    }

    @Override
    public void update(List<AgEndpoint> list) throws Exception{
        Session session = sessionFactory.getCurrentSession();
        for(AgEndpoint agEndpoint : list){
            session.update(agEndpoint);
        }
    }
}
