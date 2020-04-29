package com.tuhalang.apigw.configuration;

import com.tuhalang.apigw.utils.ConfigApp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource({"classpath:redis.properties"})
public class RedisConfiguration {

    private static final Logger LOGGER = Logger.getLogger(RedisConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        return poolConfig;
    }

    @Bean
    public JedisSentinelPool jedisSentinelPool(JedisPoolConfig poolConfig) {
        LOGGER.info("----------------------Init Jedis--------------------");
        String ips = environment.getRequiredProperty(ConfigApp.REDIS_SERVER);
        String masterName = environment.getRequiredProperty(ConfigApp.MASTER_NAME);
        String[] ipRedis = ips.split(";");
        Set<String> sentinels = new HashSet<String>();
        for (String ip : ipRedis) {
            sentinels.add(ip);
        }
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig);
        LOGGER.info("----------------------End Init Jedis--------------------");
        return jedisSentinelPool;

    }


}
