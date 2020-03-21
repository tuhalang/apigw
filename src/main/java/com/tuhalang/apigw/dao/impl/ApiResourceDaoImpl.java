package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.ApiResourceDao;
import com.tuhalang.apigw.domain.Account;
import com.tuhalang.apigw.domain.ApiResource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("api_resource_dao")
@Transactional(rollbackOn = Exception.class)
public class ApiResourceDaoImpl implements ApiResourceDao {


    @Autowired
    @Qualifier("apigw_session")
    private SessionFactory sessionFactory;

    @Override
    public ApiResource findbyApiName(String apiName) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT a FROM ApiResource a WHERE a.apiName=:apiName"
        );
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql.toString(), ApiResource.class);
        query.setParameter("apiName", apiName);
        List<ApiResource> apiResources = query.list();
        if(apiResources.size()>0){
            return apiResources.get(0);
        }
        return null;
    }
}
