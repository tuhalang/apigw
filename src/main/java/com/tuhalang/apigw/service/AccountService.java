package com.tuhalang.apigw.service;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.domain.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public interface AccountService {
    void register(UserBean userBean, ResponseBean result) throws Exception;
    void login(LinkedHashMap<String, Object> wsRequest, HttpServletRequest request, ResponseBean result) throws Exception;
    Account findByUsername(String username) throws Exception;

}
