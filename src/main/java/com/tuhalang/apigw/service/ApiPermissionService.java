package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.ApiPermission;

public interface ApiPermissionService {

    ApiPermission getPermission(String username, String apiName) throws Exception;
}
