package com.tuhalang.apigw.dao;

import com.tuhalang.apigw.domain.AgEndpoint;

import java.util.List;

public interface AgEndpointDao {

    void updateTimeAccess(AgEndpoint agEndpoint) throws Exception;

    long count() throws Exception;

    List<AgEndpoint> find(int start, int size) throws Exception;
    void update(List<AgEndpoint> list) throws Exception;
}
