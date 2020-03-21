package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.bean.RequestBean;
import com.tuhalang.apigw.bean.ResponseBean;
import org.springframework.http.ResponseEntity;

public abstract class CommonController {

    protected ResponseEntity returnSuccess(RequestBean request, ResponseBean bean){
        return ResponseEntity.ok(bean);
    }

    protected ResponseEntity returnError(RequestBean request, Exception e){
        return ResponseEntity.ok(e);
    }
}
