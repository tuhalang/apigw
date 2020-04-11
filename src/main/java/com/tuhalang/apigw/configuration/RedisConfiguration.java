package com.tuhalang.apigw.configuration;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfiguration {

    private static final Logger LOGGER = Logger.getLogger(RedisConfiguration.class);

    @Bean
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        return poolConfig;
    }

    @Bean
    public JedisSentinelPool jedisSentinelPool(JedisPoolConfig poolConfig) {
        LOGGER.info("----------------------Init Jedis--------------------");
        String[] ipRedis = {"192.168.56.102:26379"};
        Set<String> sentinels = new HashSet<String>();
        for (String ip : ipRedis) {
            sentinels.add(ip);
        }
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels, poolConfig);
        LOGGER.info("----------------------End Init Jedis--------------------");
        return jedisSentinelPool;

    }


}
