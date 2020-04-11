package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgRole;

public interface AgRoleDao {
    AgRole findByRoleName(String roleName) throws Exception;
}
