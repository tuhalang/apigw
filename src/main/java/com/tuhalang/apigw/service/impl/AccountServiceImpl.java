package com.tuhalang.apigw.service.impl;

import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.common.JwtUtils;
import com.tuhalang.apigw.dao.AccountDao;
import com.tuhalang.apigw.domain.Account;
import com.tuhalang.apigw.domain.RoleName;
import com.tuhalang.apigw.domain.UserRole;
import com.tuhalang.apigw.service.AccountService;
import com.tuhalang.apigw.service.UserRoleService;
import com.tuhalang.apigw.utils.Convertor;
import com.tuhalang.apigw.utils.ErrorCode;
import com.tuhalang.apigw.utils.SendOTP;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("account_service")
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    @Qualifier("account_dao")
    private AccountDao accountDao;

    @Autowired
    @Qualifier("user_role_service")
    private UserRoleService userRoleService;

    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    @Override
    public void register(UserBean userBean, ResponseBean result) throws Exception{
        if(validUser(userBean, result)){
            LinkedHashMap<String, Object> params = new LinkedHashMap<>();
            params.put("otp", userBean.getOtp());
            if(checkOTP(params, userBean.getUsername(), userBean.getUsername(), result)){
                return;
            }
            Date now = new Date();
            Account account = new Account();
            account.setUsername(userBean.getUsername());
            account.setEmail(userBean.getEmail());
            String salt = BCrypt.gensalt();
            String hashed = BCrypt.hashpw(userBean.getPassword()+salt, BCrypt.gensalt());
            account.setSalt(salt);
            account.setPassword(hashed);
            account.setStatus(true);
            account.setCreatedOn(now);
            account.setUpdatedOn(now);
            accountDao.save(account);

            UserRole userRole = new UserRole();
            userRole.setAccountId(account.getAccountId());
            userRole.setRoleName(RoleName.GUEST.name());
            userRoleService.save(userRole);
            result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
            result.setMessage("Register successfully !");
        }
    }

    @Override
    public void login(LinkedHashMap<String, Object> wsRequest, HttpServletRequest request, ResponseBean result) throws Exception {

        if(!wsRequest.containsKey("username")){
            LOGGER.error("missing username");
            result.setMessage("missing username");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            return;
        }
        if(!wsRequest.containsKey("password")){
            LOGGER.error("missing password");
            result.setMessage("missing password");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            return;
        }

        String username = wsRequest.get("username").toString();
        String password = wsRequest.get("password").toString();
        String remoteAddr = request.getRemoteAddr();

        Jedis jedis = jedisSentinelPool.getResource();
        String blockJson = jedis.get(remoteAddr);
        if(blockJson != null){
            Map<String, Object> map = Convertor.jsonToMap(blockJson);
            String blockUser = map.get("username").toString();
            int times = (int) map.get("times");
            if(times > 3) {
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("you are blocked !");
                jedis.set(remoteAddr, Convertor.objectToJson(map));
                jedis.expire(remoteAddr, 300);
                return;
            }
        }


        Account account = accountDao.findByUsername(username);
        if(account != null){
            String salt = account.getSalt();
            if(BCrypt.checkpw(password+salt, account.getPassword())){

                if(!checkOTP(wsRequest, username, account.getEmail(), result)){
                    return;
                };

                LOGGER.info("User " + username + " login successfully !");
                result.setErrorCode(ErrorCode.ERROR_CODE_OK.getKey());
                result.setMessage("Login Successfully !");
                String sessionId = UUID.randomUUID().toString();
                String tokenKey = JwtUtils.generateKey();
                String token = JwtUtils.createJWT(sessionId, username, 600000, tokenKey);
                String remoteAddress = request.getRemoteAddr();

                Map<String, Object> sessionCache = new HashMap<>();
                sessionCache.put("tokenKey", tokenKey);
                sessionCache.put("remoteAddress", remoteAddress);
                jedis.set(sessionId, Convertor.objectToJson(sessionCache));
                jedis.expire(sessionId, 600);
                jedis.del(remoteAddr);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("sessionId", sessionId);
                result.setWsResponse(response);
            }else {
                checkWrongPassTimes(request, username, result);
                LOGGER.info("User " + username + ": Wrong password !");
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("Wrong password !");
            }
        }else{
            LOGGER.error("User " + username + " is not exist !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("User " + username + " is not exist !");
        }
    }

    private void checkWrongPassTimes(HttpServletRequest request, String username, ResponseBean result) {
        Jedis jedis = jedisSentinelPool.getResource();
        String remoteAddr = request.getRemoteAddr();
        String blockJson = jedis.get(remoteAddr);
        if(blockJson != null){
            Map<String, Object> map = Convertor.jsonToMap(blockJson);
            int times = (int) map.get("times");
            map.put("times", times+1);
            jedis.set(remoteAddr, Convertor.objectToJson(map));
            jedis.expire(remoteAddr, 300);
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("times", 1);
            jedis.set(remoteAddr, Convertor.objectToJson(map));
            jedis.expire(remoteAddr, 300);
        }
    }

    private boolean checkOTP(LinkedHashMap<String, Object> wsRequest, String username, String email, ResponseBean result) {
        Jedis jedis = jedisSentinelPool.getResource();
        if(!wsRequest.containsKey("otp") || wsRequest.get("otp").toString().equals("")){
             String userJson = jedis.get(username);
             if(userJson == null) {
                 String otp = SendOTP.sendOTPViaEmail(email);
                 Map<String, Object> map = new HashMap<>();
                 map.put("times", 1);
                 map.put("timeSend", System.currentTimeMillis());
                 map.put("otp", otp);
                 jedis.set(username, Convertor.objectToJson(map));
                 jedis.expire(username, 120);
                 result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                 result.setMessage("please enter otp!");
                 return false;
             }
             Map<String, Object> map = Convertor.jsonToMap(userJson);
             int time = (int) map.get("times");
             if(time > 3){
                 result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                 result.setMessage("max time send otp code, please wait!");
                 return false;
             }else{
                 time+=1;
                 String otp = SendOTP.sendOTPViaEmail(email);
                 map.put("times", time);
                 map.put("timeSend", System.currentTimeMillis());
                 map.put("otp", otp);
                 jedis.set(username, Convertor.objectToJson(map));
                 jedis.expire(username, 120);
             }
             result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
             result.setMessage("please enter otp code");
             return false;
        }else{
            String userJson = jedis.get(username);
            if(userJson == null) {
                result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                result.setMessage("wrong otp!");
                return false;
            }else{
                Map<String, Object> map = Convertor.jsonToMap(userJson);
                String otp = map.get("otp").toString();
                if(otp.equals(wsRequest.get("otp").toString())){
                    jedis.del(username);
                    return true;
                }else {
                    result.setMessage("wrong otp");
                    result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
                    return false;
                }
            }

        }
    }


    @Override
    public Account findByUsername(String username) throws Exception{
        return accountDao.findByUsername(username);
    }

    private boolean validUser(UserBean userBean, ResponseBean result){
        if(StringUtils.isEmpty(userBean.getUsername())){
            LOGGER.error("Username is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Username is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(userBean.getPassword())){
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(userBean.getValidPassword())){
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(userBean.getEmail())){
            LOGGER.error("Email is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Email is invalid !");
            return false;
        }
        if(!userBean.getPassword().equals(userBean.getValidPassword())){
            LOGGER.error("Password is invalid !");
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("Password is invalid !");
            return false;
        }
        if(StringUtils.isEmpty(userBean.getOtp())){
            LOGGER.error("Missing OTP");
            String otp = SendOTP.sendOTPViaEmail(userBean.getEmail());
            LOGGER.info("SENT OTP: " + otp);
            result.setErrorCode(ErrorCode.ERROR_CODE_DEFAULT.getKey());
            result.setMessage("ENTER OTP CODE: ");
            return false;
        }
        return true;
    }


}
