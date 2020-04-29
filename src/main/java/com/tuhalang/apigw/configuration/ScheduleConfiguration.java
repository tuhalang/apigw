package com.tuhalang.apigw.configuration;

import com.tuhalang.apigw.service.AgEndpointService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ScheduleConfiguration.class);

    @Autowired
    private AgEndpointService agEndpointService;

    @Bean
    public void runHealthyCheck(){
        try {
            agEndpointService.healthyCheck();
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }
}
