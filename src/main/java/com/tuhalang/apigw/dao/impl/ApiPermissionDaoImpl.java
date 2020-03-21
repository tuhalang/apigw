package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.ApiPermissionDao;
import com.tuhalang.apigw.domain.ApiPermission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("api_permission_dao")
@Transactional(rollbackOn = Exception.class)
public class ApiPermissionDaoImpl implements ApiPermissionDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public ApiPermission getPermission(String username, String apiName) throws Exception{
        StringBuilder sql = new StringBuilder(
                "FROM ApiPermission s WHERE s.username=:username AND s.apiName=:apiName"
        );
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql.toString());
        query.setParameter("username", username);
        query.setParameter("apiName", apiName);
        List<ApiPermission> list = query.getResultList();
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
