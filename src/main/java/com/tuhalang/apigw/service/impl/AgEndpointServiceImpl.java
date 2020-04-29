package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.common.ApiUtils;
import com.tuhalang.apigw.common.CallAPI;
import com.tuhalang.apigw.dao.AgEndpointDao;
import com.tuhalang.apigw.domain.AgEndpoint;
import com.tuhalang.apigw.service.AgEndpointService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgEndpointServiceImpl implements AgEndpointService {

    private static final Logger LOGGER = Logger.getLogger(AgEndpointServiceImpl.class);

    @Autowired
    private AgEndpointDao agEndpointDao;

    @Override
    public void updateTimeAccess(AgEndpoint agEndpoint) throws Exception {
        agEndpointDao.updateTimeAccess(agEndpoint);
    }

    @Autowired
    @Scheduled(fixedRate = 300000)
    public void healthyCheck() throws Exception{
        LOGGER.info("===========START CHECK HEALTHY API===========");
        Long numOfEndpoint = agEndpointDao.count();
        int sizeBatch = 10;
        int times = (int) (numOfEndpoint / sizeBatch) + 1;
        for(int i=0; i<times; i++){
            List<AgEndpoint> list = agEndpointDao.find(i,sizeBatch);
            List<AgEndpoint> listUpdate = new ArrayList<>();
            if(!list.isEmpty()){
                for(AgEndpoint agEndpoint : list){
                    LOGGER.info("CHECK ENDPOINT: " + agEndpoint.getEndpoint());
                    String resp = ApiUtils.callHealthyCheck(agEndpoint.getEndpoint(), agEndpoint.getUrlHealthyCheck());
                    LOGGER.info("RESPONSE: " + resp);
                    agEndpoint.setStatus(resp);
                    listUpdate.add(agEndpoint);
                }
                agEndpointDao.update(listUpdate);
            }
        }
        LOGGER.info("===========END CHECK HEALTHY API===========");
    }
}
