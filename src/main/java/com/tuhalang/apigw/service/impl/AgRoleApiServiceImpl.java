package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.dao.AgRoleApiDao;
import com.tuhalang.apigw.domain.*;
import com.tuhalang.apigw.service.AgRoleApiService;
import com.tuhalang.apigw.utils.JedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class AgRoleApiServiceImpl implements AgRoleApiService {

    @Autowired
    private AgRoleApiDao agRoleApiDao;

    @Override
    public void save(AgRoleApi agRoleApi) throws Exception {
        agRoleApiDao.save(agRoleApi);
    }

    @Override
    public Boolean isPermission(AgUser agUser, AgApi agApi, Jedis jedis, ResponseBean bean) throws Exception {
        AgRoleApi agRoleApi = agRoleApiDao.findAgRoleApi(agUser.getAgUserId(), agApi.getAgApp().getAgAppId(),
                agApi.getAgService().getAgServiceId(), agApi.getAgApiId());
        if (agRoleApi != null) {
            AgRole agRole = agRoleApi.getAgRole();
            AgThrottling agThrottling = agRoleApi.getAgThrottling();

            if(agThrottling == null){
                return true;
            }

            jedis.select(JedisDB.JEDIS_DB_THROTTLING.getKey());
            if (agRole.getStatus() && (agRole.getAcceptMethod() & agApi.getMethod()) != 0) {


                // check throttling
                if (agThrottling.getStatus()) {
                    String key = agUser.getUsername() + agApi.getApiName();
                    String timesStr = jedis.get(key);
                    if (timesStr != null) {
                        long times = Long.valueOf(timesStr);
                        if (times > agThrottling.getNumOfRequest()) {
                            return false;
                        } else {
                            jedis.incr(key);
                            return true;
                        }
                    } else {
                        jedis.set(key, String.valueOf(1));
                        jedis.expire(key, agThrottling.getTimeSecond());
                        return true;
                    }
                }

            }
        }
        return false;
    }
}
