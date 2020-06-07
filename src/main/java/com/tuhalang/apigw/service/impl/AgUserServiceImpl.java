package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.common.JwtUtils;
import com.tuhalang.apigw.dao.AgUserDao;
import com.tuhalang.apigw.domain.*;
import com.tuhalang.apigw.service.*;
import com.tuhalang.apigw.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class AgUserServiceImpl implements AgUserService {

    private static final Logger LOGGER = Logger.getLogger(AgUserServiceImpl.class);

    @Autowired
    private AgUserDao agUserDao;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private AgRoleService agRoleService;

    @Autowired
    private AgAppService agAppService;

    @Autowired
    private AgServiceService agServiceService;

    @Autowired
    private AgRoleApiService agRoleApiService;

    @Override
    public void register(UserBean userBean, String ipAddr, ResponseBean result) throws Exception {

        Jedis jedis = null;
        try {

            if (!isValidUser(userBean, result)){
                return;
            }

            // get connection
            jedis = jedisService.getConncetion();

            // select from register db
            jedis.select(JedisDB.JEDIS_DB_REGISTER.getKey());


            // load user register from cache
            String agUserCacheJson = jedis.get(userBean.getEmail());

            if (agUserCacheJson != null && !StringUtils.isEmpty(userBean.getOtp())) {

                AgUser agUser = Convertor.jsonToObject(agUserCacheJson, AgUser.class);

                // select from otp db
                jedis.select(JedisDB.JEDIS_DB_OTP_REGISTER.getKey());

                String otpJsonCache = jedis.get(agUser.getEmail());

                if (otpJsonCache != null) {
                    Map<String, Object> map = Convertor.jsonToMap(otpJsonCache);
                    String otp = map.get("otp").toString();
                    if (otp.equalsIgnoreCase(userBean.getOtp())) {
                        jedis.del(agUser.getEmail());
                        saveDefaultUser(agUser);
                        result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
                        result.setMessage("Register successfully !");

                        return;
                    } else {
                        result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                        result.setMessage("wrong otp!");
                        return;
                    }
                }
            }

            // check if exist username or email
            if (agUserDao.isExistEmailOrUsername(userBean.getEmail(), userBean.getUsername())) {
                LOGGER.error("username or email is exist !");
                result.setMessage("username or email is exist !");
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                return;
            }

            // send otp code
            jedis.select(JedisDB.JEDIS_DB_OTP.getKey());

            // get otp code in redis cache
            String otpJsonCache = jedis.get(userBean.getEmail());

            // if not exist
            if (otpJsonCache == null) {

                // send otp code
                String otp = SendOTP.sendOTPViaEmail(userBean.getEmail(), kafkaService);

                // cache otp code && num of times in redis
                Map<String, Object> map = new HashMap<>();
                map.put("times", 1);
                map.put("time_send", System.currentTimeMillis());
                map.put("otp", otp);

                // cache user register
                Date now = new Date();
                AgUser agUser = new AgUser();
                agUser.setAgUserId(UUID.randomUUID());
                agUser.setUsername(userBean.getUsername());
                agUser.setEmail(userBean.getEmail());
                String salt = BCrypt.gensalt();
                String hashed = BCrypt.hashpw(userBean.getPassword() + salt, BCrypt.gensalt());
                agUser.setSalt(salt);
                agUser.setPassword(hashed);
                agUser.setStatus(true);
                agUser.setCreatedDate(now);
                agUser.setUpdatedDate(now);
                agUser.setCreatedBy("SYSTEM");
                agUser.setUpdatedBy("SYSTEM");

                jedis.select(JedisDB.JEDIS_DB_OTP_REGISTER.getKey());
                jedis.set(userBean.getEmail(), Convertor.objectToJson(map));
                jedis.expire(userBean.getEmail(), ConfigApp.OTP_CACHE_TIME);

                jedis.select(JedisDB.JEDIS_DB_REGISTER.getKey());
                jedis.set(userBean.getEmail(), Convertor.objectToJson(agUser));
                jedis.expire(userBean.getEmail(), ConfigApp.USER_REGISTER_CACHE_TIME);

                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("please enter otp!");
                return;
            }

            // if exist
            Map<String, Object> map = Convertor.jsonToMap(otpJsonCache);

            // get num of times send otp
            int times = (int) map.get("times");

            // if the number of times has reached the limit
            if (times > ConfigApp.MAX_TIMS_SEND_OTP_PER_DAY) {
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("max times send otp code, please wait!");
                return;
            } else {

                // get last time send otp
                Long lastSend = (Long) map.get("time_send");


                if (lastSend + ConfigApp.MIN_DISTANCE_BETWEEN_TWO_TIME_SEND_OTP < System.currentTimeMillis()) {
                    times += 1;
                    String otp = SendOTP.sendOTPViaEmail(userBean.getEmail(), kafkaService);
                    map.put("times", times);
                    map.put("time_send", System.currentTimeMillis());
                    map.put("otp", otp);
                    jedis.set(userBean.getEmail(), Convertor.objectToJson(map));
                    jedis.expire(userBean.getEmail(), ConfigApp.OTP_CACHE_TIME);
                }
            }
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("please enter otp code");

        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
    }

    @Override
    public void login(LinkedHashMap<String, Object> wsRequest, String ipAddr, ResponseBean result) throws Exception {

        if (!wsRequest.containsKey("username")) {
            LOGGER.error("missing username");
            result.setMessage("missing username");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            return;
        }
        if (!wsRequest.containsKey("password")) {
            LOGGER.error("missing password");
            result.setMessage("missing password");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            return;
        }


        Jedis jedis = null;
        try {
            jedis = jedisService.getConncetion();

            //check user in black list
            jedis.select(JedisDB.JEDIS_DB_BLACK_LIST.getKey());
            String isBlock = jedis.get(ipAddr);
            if (isBlock != null) {
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("you are blocked !");
                return;
            }

            //auth account
            String username = wsRequest.get("username").toString();
            String password = wsRequest.get("password").toString();
            AgUser agUser = agUserDao.findByUsername(username);
            if (agUser != null) {
                String salt = agUser.getSalt();
                if (BCrypt.checkpw(password + salt, agUser.getPassword())) {

                    //check user in jedis cache
                    jedis.select(JedisDB.JEDIS_DB_LOGIN.getKey());
                    Map<String, String> infoLogin = jedis.hgetAll(username);
                    if (!infoLogin.isEmpty()) {
                        result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
                        result.setMessage("Login Successfully !");
                        result.setWsResponse(infoLogin);
                        return;
                    }

                    //gen token, session
                    String sessionId = UUID.randomUUID().toString();
                    String tokenKey = JwtUtils.generateKey();
                    String token = JwtUtils.createJWT(sessionId, username, 600000000, tokenKey);

                    Map<String, String> sessionCache = new HashMap<>();
                    sessionCache.put("token", token);
                    sessionCache.put("remoteAddress", ipAddr);
                    //cache something more
                    jedis.select(JedisDB.JEDIS_DB_SESSION.getKey());
                    jedis.hmset(sessionId, sessionCache);
                    jedis.expire(sessionId, 600000);
                    jedis.del(ipAddr);

                    //cache info login
                    Map<String, String> response = new HashMap<>();
                    response.put("token", tokenKey);
                    response.put("sessionId", sessionId);
                    jedis.select(JedisDB.JEDIS_DB_LOGIN.getKey());
                    jedis.hmset(username, response);
                    jedis.expire(username, 600000);

                    //response
                    result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
                    result.setMessage("Login Successfully !");
                    result.setWsResponse(response);

                } else {
                    checkWrongPassTimes(ipAddr, username, jedis);
                    LOGGER.info("User " + username + ": Wrong password !");
                    result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    result.setMessage("Wrong password !");
                }
            } else {
                LOGGER.error("User " + username + " is not exist !");
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("User " + username + " is not exist !");
            }

        } catch (Exception e) {

        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }

    }

    @Override
    public Boolean saveDefaultUser(AgUser agUser) throws Exception {
        return agUserDao.saveDefaultUser(agUser.getUsername(), agUser.getPassword(), agUser.getSalt(), agUser.getEmail());
    }

    @Override
    public AgUser findByUsername(String username) throws Exception {
        return agUserDao.findByUsername(username);
    }

    private void checkWrongPassTimes(String ipAddr, String identify, Jedis jedis) {
        try {
            // select from black list db
            jedis.select(JedisDB.JEDIS_DB_BLACK_LIST.getKey());

            String key = ipAddr + identify;

            jedis.incr(key);
            jedis.expire(key, 120);

            int times = Integer.parseInt(jedis.get(key));
            if (times >= ConfigApp.MAX_TIMES_WRONG_PASS) {
                jedis.set(ipAddr, "blocked");
                jedis.expire(ipAddr, ConfigApp.BACK_LIST_BLOCK_TIME);
                jedis.del(key);
            }
        } catch (Exception e) {

        }
    }

    private boolean isValidUser(UserBean userBean, ResponseBean result) {
        if (StringUtils.isEmpty(userBean.getUsername())) {
            LOGGER.error("Username is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Username is invalid !");
            return false;
        }
        if (StringUtils.isEmpty(userBean.getPassword())) {
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        if (StringUtils.isEmpty(userBean.getValidPassword())) {
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        if (StringUtils.isEmpty(userBean.getEmail())) {
            LOGGER.error("Email is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Email is invalid !");
            return false;
        }
        if (!userBean.getPassword().equals(userBean.getValidPassword())) {
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        return true;
    }


    @Override
    public void update(AgUser agUser) throws Exception {
        agUserDao.update(agUser);
    }

    @Override
    public void registerService(AgUser agUser, String serviceCode, String roleName, ResponseBean result) throws Exception {
        AgRole agRole = agRoleService.findByRoleName(roleName);
        if(agRole == null){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Role not found !");
            return;
        }

//        AgApp agApp = agAppService.findByAppName(appCode);
//        if(agApp == null){
//            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
//            result.setMessage("appCode not found !");
//            return;
//        }

        AgService agService = agServiceService.findByServiceName(serviceCode);
        if(agService == null){
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("appCode not found !");
            return;
        }

        AgRoleApi agRoleApi = new AgRoleApi();
        agRoleApi.setAgRoleApiId(UUID.randomUUID());
        //agRoleApi.setAgApp(agApp);
        agRoleApi.setAgService(agService);
        agRoleApi.setAgUser(agUser);
        agRoleApi.setAgRole(agRole);
        agRoleApi.setStatus(Boolean.TRUE);

        agRoleApiService.save(agRoleApi);

        result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
        result.setMessage("Register service successfully !");
    }
}
