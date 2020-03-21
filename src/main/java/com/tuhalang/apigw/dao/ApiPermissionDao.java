package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.ApiPermission;

public interface ApiPermissionDao {
    ApiPermission getPermission(String username, String apiName) throws Exception;
}
