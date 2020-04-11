package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.common.CallAPI;
import com.tuhalang.apigw.common.JwtUtils;
import com.tuhalang.apigw.domain.ApiPermission;
import com.tuhalang.apigw.domain.ApiResource;
import com.tuhalang.apigw.service.AgUserService;
import com.tuhalang.apigw.service.ApiPermissionService;
import com.tuhalang.apigw.service.ApiResourceService;
import com.tuhalang.apigw.utils.Convertor;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
public class RoutingController extends CommonController{

    @Autowired
    private ApiResourceService apiResourceService;

    @Autowired
    private AgUserService agUserService;

    @Autowired
    private ApiPermissionService apiPermissionService;

    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    @RequestMapping(value = "/routing", method = RequestMethod.POST)
    public ResponseEntity<ResponseBean> routing(@RequestBody RequestBean requestBean, HttpServletRequest request){
        try{
            ResponseBean bean = new ResponseBean();
            String sessionId = requestBean.getSession();
            String token = requestBean.getToken();
            if(sessionId == null || token == null){
                return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            }

            Jedis jedis = jedisSentinelPool.getResource();
            String sessionCache = jedis.get(sessionId);
            if(sessionCache == null){
                return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            }

            Map<String, Object> map = Convertor.jsonToMap(sessionCache);
            String tokenKey = map.get("tokenKey").toString();
            Claims claims = JwtUtils.decodeJWT(token, tokenKey);
            if(claims == null){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Date expire = claims.getExpiration();
            Date now = new Date();
            if(expire.before(now)){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String username = claims.getSubject();
            String apiName = requestBean.getApiName();

            ApiPermission apiPermission = apiPermissionService.getPermission(username, apiName);

            if(apiPermission == null || !apiPermission.getHasPermission()){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String timesStr = jedis.get(username+apiName);
            if(timesStr != null){
                long times = Long.valueOf(timesStr);
                if(times > apiPermission.getThrottling()){
                    return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
                }else{
                    jedis.incr(username+apiName);
                }
            }else {
                jedis.set(username+apiName, String.valueOf(1));
                jedis.expire(username+apiName, 60);
            }

            ApiResource apiResource = apiResourceService.findbyApiName(apiName);


            CallAPI callAPI = new CallAPI(requestBean.getWsRequest(), apiResource.getEndPoint(), apiResource.getResource(),
                    apiResource.getApiKey(), username, apiName, 0, apiResource.getTimeout());
            Thread thread = new Thread(callAPI);
            thread.start();
            Long endTimeMillis = System.currentTimeMillis() + 60000;
            while (thread.isAlive()){
                if (System.currentTimeMillis() > endTimeMillis) {
                    try {
                        thread.stop();
                    } catch (Exception e) {
                    }
                } else if (callAPI.getFinish()) {
                    break;
                }

                try {
                    Thread.sleep(100);
                    Thread.yield();
                } catch (Exception e) {
                }
            }
            bean.setWsResponse(callAPI.getResultObject());

            return returnSuccess(requestBean, bean);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }
}
