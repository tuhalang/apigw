package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class JedisServiceImpl implements JedisService {

    @Autowired
    private JedisSentinelPool jedisSentinelPool;



    private Jedis getRealConncetion() throws Exception {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            //jedis.auth(REDIS_SENTINEL_PASS);
            return jedis;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Jedis getConncetion() throws Exception {
        Jedis jedis = getRealConncetion();
        if (jedis != null) {
            return jedis;
        }
        else {
            int retryTime = 0;
            while (retryTime++ < 3) {
                try {
                    jedis = getRealConncetion();
                    if (jedis != null) {
                        return jedis;
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
            }
            return null;
        }
    }
}
