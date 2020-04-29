package com.tuhalang.apigw.service;


import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.domain.AgApi;
import com.tuhalang.apigw.domain.AgRoleApi;
import com.tuhalang.apigw.domain.AgUser;
import redis.clients.jedis.Jedis;

public interface AgRoleApiService {

    void save(AgRoleApi agRoleApi) throws Exception;
    Boolean isPermission(AgUser agUser, AgApi agApi, Jedis jedis, ResponseBean bean) throws Exception;
}
