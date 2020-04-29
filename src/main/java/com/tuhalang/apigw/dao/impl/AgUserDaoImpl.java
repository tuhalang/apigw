package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AgUserDao;
import com.tuhalang.apigw.domain.AgUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.OneToMany;
import javax.transaction.Transactional;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.Callable;

@Repository
@Transactional(rollbackOn = Exception.class)
public class AgUserDaoImpl implements AgUserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Boolean saveDefaultUser(String username, String password, String salt, String email) throws Exception{
        Session session = sessionFactory.getCurrentSession();
        return session.doReturningWork(connection -> {
            CallableStatement call = connection.prepareCall(
                    "select create_new_user(?, ?, ?, ?)"
            );
            call.setString(1, username);
            call.setString(2, password);
            call.setString(3, salt);
            call.setString(4, email);
            return call.execute();
        });
    }

    @Override
    public void update(AgUser agUser) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.update(agUser);
    }

    @Override
    public void save(AgUser agUser) throws Exception{
        Session session = sessionFactory.getCurrentSession();
        session.save(agUser);
    }

    @Override
    public AgUser findByUsername(String username) throws Exception{
        String sql = "SELECT a FROM AgUser a INNER JOIN FETCH a.agRoleApis WHERE a.username=:username";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql, AgUser.class);
        query.setParameter("username", username);
        List<AgUser> agUsers = query.list();
        if(agUsers.size()>0){
            return agUsers.get(0);
        }
        return null;
    }

    @Override
    public Boolean isExistEmailOrUsername(String email, String username) throws Exception {
        StringBuilder sql = new StringBuilder(
                "SELECT 1 FROM ag_user a WHERE a.username=:username or a.email=:email"
        );
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery(sql.toString());
        query.setParameter("username", username);
        query.setParameter("email", email);
        List<Object> result = query.list();
        if(result.size()>0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
