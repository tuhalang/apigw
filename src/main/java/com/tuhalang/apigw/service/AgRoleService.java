package com.tuhalang.apigw.service;

import com.tuhalang.apigw.domain.AgRole;

public interface AgRoleService {
    AgRole findByRoleName(String roleName) throws Exception;
}
