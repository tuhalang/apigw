package com.tuhalang.apigw.service;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.domain.AgUser;

import java.util.LinkedHashMap;

public interface AgUserService {
    void register(UserBean userBean, String ipAddr, ResponseBean result) throws Exception;
    void login(LinkedHashMap<String, Object> wsRequest, String ipAddr, ResponseBean result) throws Exception;
    Boolean saveDefaultUser(AgUser agUser) throws Exception;
    AgUser findByUsername(String username) throws Exception;

    void update(AgUser agUser) throws Exception;
}
