package com.tuhalang.apigw.service;

import redis.clients.jedis.Jedis;

public interface JedisService {
    Jedis getConncetion() throws Exception;
}
