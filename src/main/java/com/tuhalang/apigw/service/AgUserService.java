package com.tuhalang.apigw.service;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.domain.Account;
import com.tuhalang.apigw.domain.AgUser;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public interface AgUserService {
    void register(UserBean userBean, String ipAddr, ResponseBean result) throws Exception;
    void login(LinkedHashMap<String, Object> wsRequest, String ipAddr, ResponseBean result) throws Exception;
    Boolean saveDefaultUser(AgUser agUser) throws Exception;

}
