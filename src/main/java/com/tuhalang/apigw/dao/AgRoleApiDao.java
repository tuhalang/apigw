package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgRoleApi;

import java.util.UUID;

public interface AgRoleApiDao {
    void save(AgRoleApi agRoleApi) throws Exception;
    AgRoleApi findAgRoleApi(UUID agUserId, UUID agAppId, UUID agServiceId, UUID agApiId) throws Exception;
}
