package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgUser;


public interface AgUserDao {
    void save(AgUser agUser) throws Exception;
    AgUser findByUsername(String username) throws Exception;
    Boolean isExistEmailOrUsername(String email, String username) throws Exception;
    Boolean saveDefaultUser(String username, String password, String salt, String email) throws Exception;

    void update(AgUser agUser) throws Exception;
}
