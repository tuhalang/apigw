package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.service.AgUserService;
import com.tuhalang.apigw.service.UserRoleService;
import com.tuhalang.apigw.bean.ResponseBean;
import com.tuhalang.apigw.bean.UserBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@RestController
public class AccountController extends CommonController{

    private static final Logger LOGGER = Logger.getLogger(AccountController.class);

    @Autowired
    private AgUserService agUserService;

    @Autowired
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
            String ipAddr = request.getLocalAddr();
            agUserService.register(userBean,ipAddr, result);
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
            String ipAddr = request.getLocalAddr();
            LinkedHashMap<String, Object> wsRequest = requestBean.getWsRequest();
            agUserService.login(wsRequest, ipAddr, result);
            return returnSuccess(requestBean, result);
        }catch (Exception e){
            return returnError(requestBean, e);
        }
    }


}
