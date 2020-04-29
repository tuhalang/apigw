package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.common.ApiUtils;
import com.tuhalang.apigw.common.CallAPI;
import com.tuhalang.apigw.common.JwtUtils;
import com.tuhalang.apigw.domain.AgApi;
import com.tuhalang.apigw.domain.AgEndpoint;
import com.tuhalang.apigw.domain.AgLog;
import com.tuhalang.apigw.domain.AgUser;
import com.tuhalang.apigw.service.*;
import com.tuhalang.apigw.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RoutingController extends CommonController{

    @Autowired
    private AgRoleApiService agRoleApiService;

    @Autowired
    private AgUserService agUserService;

    @Autowired
    private AgApiService agApiService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private AgEndpointService agEndpointService;

    @RequestMapping(value = "/wsRouting", method = RequestMethod.POST)
    public ResponseEntity<ResponseBean> routing(@RequestBody RequestBean requestBean, HttpServletRequest request){

        AgLog agLog = new AgLog();
        Long start = System.currentTimeMillis();
        agLog.setStartTime(new Date());
        agLog.setIp(ApiUtils.getClientIpAddress(request));
        agLog.setRequest(Convertor.objectToJson(requestBean));

        try{
            ResponseBean bean = new ResponseBean();
            if(!isValidRequest(requestBean, bean)){
                return returnSuccess(requestBean, bean);
            }

            Jedis jedis = jedisService.getConncetion();
            jedis.select(JedisDB.JEDIS_DB_SESSION.getKey());
            Map<String, String> sessionCache = jedis.hgetAll(requestBean.getSession());

            if(sessionCache.isEmpty()){
                bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                bean.setMessage("session is not exist !");
                return returnSuccess(requestBean, bean);
            }

            String token = sessionCache.get("token").toString();
            Claims claims = JwtUtils.decodeJWT(token, requestBean.getToken());

            if(claims == null){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Date expire = claims.getExpiration();
            Date now = new Date();
            if(expire.before(now)){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String username = claims.getSubject();
            if(!username.equals(requestBean.getUsername())){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // check role access api
            AgUser agUser = agUserService.findByUsername(username);
            AgApi agApi = agApiService.findByApiName(requestBean.getApiName());

            if(agApi == null || !agApi.getStatus()){
                bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                bean.setMessage("API not found !");
                return new ResponseEntity<>(bean, HttpStatus.NOT_FOUND);
            }

            if(agApi.getAuth()){
                if(StringUtils.isEmpty(requestBean.getApiKey()) || !agApi.getApiKey().equals(requestBean.getApiKey())){
                    bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    bean.setMessage("Wrong apiKey");
                    return new ResponseEntity<>(bean, HttpStatus.UNAUTHORIZED);
                }
            }

            if(!agRoleApiService.isPermission(agUser, agApi, jedis, bean)){
                return returnSuccess(requestBean, bean);
            }

            if(agApi.getOtp()){

                jedis.select(JedisDB.JEDIS_DB_OTP_ROUTING.getKey());
                String key = requestBean.getSession() + requestBean.getApiName();
                String otpJsonCache = jedis.get(key);

                if(otpJsonCache == null){
                    //send otp
                    String otp = SendOTP.sendOTPViaEmail(agUser.getEmail(), kafkaService);

                    // cache otp code && num of times in redis
                    Map<String, Object> map = new HashMap<>();
                    map.put("times", 1);
                    map.put("time_send", System.currentTimeMillis());
                    map.put("otp", otp);

                    jedis.set(key, Convertor.objectToJson(map));
                    jedis.expire(key, ConfigApp.OTP_CACHE_TIME);

                    //return
                    bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    bean.setMessage("Enter otp");
                    return new ResponseEntity<>(bean, HttpStatus.OK);
                }

                Map<String, Object> map = Convertor.jsonToMap(otpJsonCache);

                //user want send otp again
                if(StringUtils.isEmpty(requestBean.getOtp())){

                    // get num of times send otp
                    int times = (int) map.get("times");

                    // if the number of times has reached the limit
                    if (times > ConfigApp.MAX_TIMS_SEND_OTP_PER_DAY) {
                        bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                        bean.setMessage("max times send otp code, please wait!");
                        return new ResponseEntity<>(bean, HttpStatus.TOO_MANY_REQUESTS);
                    } else {

                        // get last time send otp
                        Long lastSend = (Long) map.get("time_send");

                        if (lastSend + ConfigApp.MIN_DISTANCE_BETWEEN_TWO_TIME_SEND_OTP < System.currentTimeMillis()) {
                            times += 1;
                            String otp = SendOTP.sendOTPViaEmail(agUser.getEmail(), kafkaService);

                            map.put("times", times);
                            map.put("time_send", System.currentTimeMillis());
                            map.put("otp", otp);
                            jedis.set(key, Convertor.objectToJson(map));
                            jedis.expire(key, ConfigApp.OTP_CACHE_TIME);
                            bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                            bean.setMessage("Enter otp");
                            return new ResponseEntity<>(bean, HttpStatus.OK);
                        }

                        bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                        bean.setMessage("please wait before request new otp");
                        return new ResponseEntity<>(bean, HttpStatus.OK);
                    }
                }

                String otp = map.get("otp").toString();
                if(!otp.equals(requestBean.getOtp())){
                    //return
                    bean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    bean.setMessage("wrong otp");
                    return new ResponseEntity<>(bean, HttpStatus.OK);
                }

                jedis.del(key);
            }
            List<AgEndpoint> agEndpoints = agApi.getEndpoints();
            if(agEndpoints.isEmpty()){
                bean.setMessage("Endpoint not found !");
                return new ResponseEntity<>(bean, HttpStatus.OK);
            }

            AgEndpoint agEndpoint = agEndpoints.get(0);


            CallAPI callAPI = new CallAPI(requestBean.getWsRequest(), agEndpoint.getEndpoint(), agApi.getResource(),
                    agApi.getApiKey(), username, agApi.getApiName(), agApi.getMethod(), agApi.getTimeout());
            Thread thread = new Thread(callAPI);
            thread.start();
            Long endTimeMillis = System.currentTimeMillis() + agApi.getTimeout();
            agEndpointService.updateTimeAccess(agEndpoint);

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
            bean.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
            bean.setWsResponse(callAPI.getResultObject());

            Long end = System.currentTimeMillis();
            agLog.setEndTime(new Date());
            agLog.setErrorCode(bean.getErrorCode());
            agLog.setProcessTime(end-start);
            agLog.setResponse(Convertor.objectToJson(bean));
            agLog.setUsername(username);
            agLog.setApiName(requestBean.getApiName());
            kafkaService.writeLogTransaction(agLog);


            return returnSuccess(requestBean, bean);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }

    public boolean isValidRequest(RequestBean requestBean, ResponseBean responseBean){
        if(StringUtils.isEmpty(requestBean.getToken())){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("token is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(requestBean.getSession())){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("session is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(requestBean.getApiName())){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("apiName is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(requestBean.getWsRequest())){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("wsRequest is invalid !");
            return false;
        }

        return true;
    }
}
