package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgRoleApiDao;
import com.tuhalang.apigw.domain.AgRoleApi;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

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

    @Override
    public AgRoleApi findAgRoleApi(UUID agUserId, UUID agAppId, UUID agServiceId, UUID agApiId) throws Exception {

        String sql = "SELECT a FROM AgRoleApi a INNER JOIN a.agUser u LEFT JOIN a.agApp b LEFT JOIN a.agService s LEFT JOIn a.agApi c"
                + " WHERE a.status=true and u.agUserId=:agUserId and (b.agAppId=:agAppId or s.agServiceId=:agServiceId or c.agApiId=:agApiId)";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgRoleApi.class);
        query.setParameter("agUserId", agUserId);
        query.setParameter("agAppId", agApiId);
        query.setParameter("agServiceId", agServiceId);
        query.setParameter("agApiId", agApiId);
        List<AgRoleApi> list = query.list();
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
