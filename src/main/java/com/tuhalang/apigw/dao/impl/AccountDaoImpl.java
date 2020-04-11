package com.tuhalang.apigw.dao.impl;

import com.tuhalang.apigw.dao.AccountDao;
import com.tuhalang.apigw.domain.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository("account_dao")
@Transactional(rollbackOn = Exception.class)
public class AccountDaoImpl implements AccountDao {

    @Autowired
    @Qualifier("apigw_session")
    private SessionFactory sessionFactory;

    @Override
    public void save(Account account) throws Exception{
        Session session = sessionFactory.getCurrentSession();
        session.save(account);
    }

    @Override
    public Account findByUsername(String username) throws Exception{
        StringBuilder sql = new StringBuilder(
                "SELECT a FROM Account a WHERE a.username=:username"
        );
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql.toString(), Account.class);
        query.setParameter("username", username);
        List<Account> accounts = query.list();
        if(accounts.size()>0){
            return accounts.get(0);
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
