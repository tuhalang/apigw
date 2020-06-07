package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.bean.RequestTest;
import com.tuhalang.apigw.common.ApiUtils;
import com.tuhalang.apigw.common.JwtUtils;
import com.tuhalang.apigw.domain.AgUser;
import com.tuhalang.apigw.service.AgUserService;
import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.service.JedisService;
import com.tuhalang.apigw.service.KafkaService;
import com.tuhalang.apigw.utils.*;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AccountController extends CommonController{

    private static final Logger LOGGER = Logger.getLogger(AccountController.class);

    @Autowired
    private AgUserService agUserService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private JedisService jedisService;




    @RequestMapping(value = "/wsTest1", method = RequestMethod.GET)
    public ResponseEntity<String> test1(HttpServletRequest request){
        String ipAddr = ApiUtils.getClientIpAddress(request);
        LOGGER.info("IPADDRESS : " + ipAddr);
        return ResponseEntity.ok(ipAddr);
    }

    @RequestMapping(value = "/healthy_check", method = RequestMethod.GET)
    public ResponseEntity<String> healthyCheck(){
        return ResponseEntity.ok("00");
    }

    @RequestMapping(value = "/wsTest2", method = RequestMethod.DELETE)
    public ResponseEntity<String> test2(){
        return ResponseEntity.ok("OK");
    }


    @RequestMapping(value = "/wsTest3", method = RequestMethod.POST)
    public ResponseEntity<String> test3(@RequestBody RequestTest request){
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/wsTest4", method = RequestMethod.PUT)
    public ResponseEntity<String> test4(@RequestBody RequestTest request){
        return ResponseEntity.ok("OK");
    }


    @RequestMapping(value = "/wsTestKafka", method = RequestMethod.GET)
    public ResponseEntity<String> testKafka(){
        kafkaService.writeLogAccess("HELLO HI HO HA");
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/wsSignUp", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity register(@RequestBody UserBean userBean, HttpServletRequest request){
        try {
            ResponseBean result = new ResponseBean();
            String ipAddr = ApiUtils.getClientIpAddress(request);
            agUserService.register(userBean,ipAddr, result);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }
    }

    @RequestMapping(value = "/wsSignIn", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login(@RequestBody RequestBean requestBean, HttpServletRequest request){
        try {
            ResponseBean result = new ResponseBean();
            String ipAddr = ApiUtils.getClientIpAddress(request);
            LinkedHashMap<String, Object> wsRequest = requestBean.getWsRequest();
            agUserService.login(wsRequest, ipAddr, result);
            return returnSuccess(requestBean, result);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }

    @RequestMapping(value = "/wsChangePass", method = RequestMethod.POST)
    public ResponseEntity changePass(@RequestBody RequestBean requestBean, HttpServletRequest request){
        try{
            ResponseBean result = new ResponseBean();
            if(!isValidRequest(requestBean, result)){
                return returnSuccess(requestBean, result);
            }

            Jedis jedis = jedisService.getConncetion();
            jedis.select(JedisDB.JEDIS_DB_SESSION.getKey());
            Map<String, String> sessionCache = jedis.hgetAll(requestBean.getSession());

            if(sessionCache.isEmpty()){
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("session is not exist !");
                return returnSuccess(requestBean, result);
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

            String ipAddr = ApiUtils.getClientIpAddress(request);
            String oldPass = requestBean.getWsRequest().get("oldPass").toString();
            String newPass = requestBean.getWsRequest().get("newPass").toString();
            AgUser agUser = agUserService.findByUsername(username);

            if (agUser != null) {
                String salt = agUser.getSalt();
                if (BCrypt.checkpw(oldPass + salt, agUser.getPassword())) {
                    jedis.select(JedisDB.JEDIS_DB_OTP_CHANGE_PASS.getKey());
                    // get otp code in redis cache
                    String otpJsonCache = jedis.get(agUser.getEmail());

                    // if not exist
                    if (otpJsonCache == null) {

                        // send otp code
                        String otp = SendOTP.sendOTPViaEmail(agUser.getEmail(), kafkaService);

                        // cache otp code && num of times in redis
                        Map<String, Object> map = new HashMap<>();
                        map.put("times", 1);
                        map.put("time_send", System.currentTimeMillis());
                        map.put("otp", otp);
                        jedis.select(JedisDB.JEDIS_DB_OTP_CHANGE_PASS.getKey());
                        jedis.set(agUser.getEmail(), Convertor.objectToJson(map));
                        jedis.expire(agUser.getEmail(), ConfigApp.OTP_CACHE_TIME);

                        result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                        result.setMessage("Please enter otp code!");
                        return returnSuccess(requestBean, result);
                    }

                    Map<String, Object> map = Convertor.jsonToMap(otpJsonCache);

                    if(!requestBean.getWsRequest().containsKey("otp") || StringUtils.isEmpty(requestBean.getWsRequest().get("otp"))){

                        // get num of times send otp
                        int times = (int) map.get("times");

                        // if the number of times has reached the limit
                        if (times > ConfigApp.MAX_TIMS_SEND_OTP_PER_DAY) {
                            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                            result.setMessage("max times send otp code, please wait!");
                        } else {

                            // get last time send otp
                            Long lastSend = (Long) map.get("time_send");


                            if (lastSend + ConfigApp.MIN_DISTANCE_BETWEEN_TWO_TIME_SEND_OTP < System.currentTimeMillis()) {
                                times += 1;
                                String otp = SendOTP.sendOTPViaEmail(agUser.getEmail(), kafkaService);
                                map.put("times", times);
                                map.put("time_send", System.currentTimeMillis());
                                map.put("otp", otp);
                                jedis.select(JedisDB.JEDIS_DB_OTP_CHANGE_PASS.getKey());
                                jedis.set(agUser.getEmail(), Convertor.objectToJson(map));
                                jedis.expire(agUser.getEmail(), ConfigApp.OTP_CACHE_TIME);
                            }

                            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                            result.setMessage("Please enter otp code!");
                        }
                        return returnSuccess(requestBean, result);
                    }

                    String otp = requestBean.getWsRequest().get("otp").toString();
                    String otpCache = map.get("otp").toString();

                    if(otp.equals(otpCache)){
                        salt = BCrypt.gensalt();
                        String hashed = BCrypt.hashpw(newPass + salt, BCrypt.gensalt());
                        agUser.setSalt(salt);
                        agUser.setPassword(hashed);
                        agUserService.update(agUser);
                        jedis.select(JedisDB.JEDIS_DB_OTP_CHANGE_PASS.getKey());
                        jedis.del(agUser.getEmail());
                        jedis.select(JedisDB.JEDIS_DB_SESSION.getKey());
                        jedis.del(requestBean.getSession());
                        result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
                        result.setMessage("Change pass successfully !");
                        return returnSuccess(requestBean, result);
                    }else{
                        result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                        result.setMessage("Wrong OTP");
                        return returnSuccess(requestBean, result);
                    }

                }else{
                    result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    result.setMessage("Wrong PASS");
                    return returnSuccess(requestBean, result);
                }
            }
            return returnSuccess(requestBean, result);
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
        if(requestBean.getWsRequest() == null || requestBean.getWsRequest().isEmpty()){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("wsRequest is invalid !");
            return false;
        }
        if(!requestBean.getWsRequest().containsKey("oldPass")){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("oldPass is invalid !");
            return false;
        }
        if(!requestBean.getWsRequest().containsKey("newPass")){
            responseBean.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            responseBean.setMessage("newPass is invalid !");
            return false;
        }

        return true;
    }

    @RequestMapping(value = "/wsRegisterService", method = RequestMethod.POST)
    public ResponseEntity registerService(@RequestBody RequestBean requestBean, HttpServletRequest request){
        try{
            ResponseBean result = new ResponseBean();
            if(!isValidRequestRegService(requestBean, result)){
                return returnSuccess(requestBean, result);
            }

            Jedis jedis = jedisService.getConncetion();
            jedis.select(JedisDB.JEDIS_DB_SESSION.getKey());
            Map<String, String> sessionCache = jedis.hgetAll(requestBean.getSession());

            if(sessionCache.isEmpty()){
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("session is not exist !");
                return returnSuccess(requestBean, result);
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

            String ipAddr = ApiUtils.getClientIpAddress(request);
            AgUser agUser = agUserService.findByUsername(username);

            String serviceCode = (String) requestBean.getWsRequest().get("serviceCode");
            String roleName = (String) requestBean.getWsRequest().get("role");

            agUserService.registerService(agUser, serviceCode, roleName, result);

            return returnSuccess(requestBean, result);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }

    private boolean isValidRequestRegService(RequestBean requestBean, ResponseBean result) {
        if(StringUtils.isEmpty(requestBean.getSession())){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("session is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(requestBean.getToken())){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("token is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(requestBean.getApiName())){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("apiName is invalid !");
            return false;
        }
        if(requestBean.getWsRequest() == null || requestBean.getWsRequest().isEmpty()){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("wsRequest is invalid !");
            return false;
        }
        if(!requestBean.getWsRequest().containsKey("serviceCode")) {
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("serviceCode is invalid !");
            return false;
        }
        if(!requestBean.getWsRequest().containsKey("role")){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("role is invalid !");
            return false;
        }
        return true;
    }
}
