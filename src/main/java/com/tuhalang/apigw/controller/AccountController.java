package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.domain.RoleName;
import com.tuhalang.apigw.domain.UserRole;
import com.tuhalang.apigw.service.AccountService;
import com.tuhalang.apigw.service.UserRoleService;
import com.tuhalang.apigw.utils.ErrorCode;
import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import com.tuhalang.apigw.domain.Account;
import com.tuhalang.apigw.utils.SendOTP;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;

@RestController
public class AccountController extends CommonController{

    private static final Logger LOGGER = Logger.getLogger(AccountController.class);

    @Autowired
    @Qualifier("account_service")
    private AccountService accountService;

    @Autowired
    @Qualifier("user_role_service")
    private UserRoleService userRoleService;



    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity register(@RequestBody UserBean userBean, HttpServletRequest request){
        try {
            ResponseBean result = new ResponseBean();
            accountService.register(userBean, result);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.ok(e);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login(@RequestBody RequestBean requestBean, HttpServletRequest request){
        try {
            ResponseBean result = new ResponseBean();
            LinkedHashMap<String, Object> wsRequest = requestBean.getWsRequest();
            accountService.login(wsRequest, request, result);
            return returnSuccess(requestBean, result);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }


}
