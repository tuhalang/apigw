package com.tuhalang.apigw.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "API_RESOURCE")
public class ApiResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "API_RESOURCE_ID")
    private Long apiResourceId;

    @Column(name = "API_NAME")
    private String apiName;

    @Column(name = "API_KEY")
    private String apiKey;

    @Column(name = "END_POINT")
    private String endPoint;

    @Column(name = "RESOURCE")
    private String resource;

    @Column(name = "HTTP_METHOD")
    private int httpMethod;

    @Column(name = "TIMEOUT")
    private Long timeout;

    @Column(name = "IS_OTP")
    private Boolean isOtp;

    @Column(name = "STATUS")
    private Boolean status;

    public Long getApiResourceId() {
        return apiResourceId;
    }

    public void setApiResourceId(Long apiResourceId) {
        this.apiResourceId = apiResourceId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(int httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Boolean getOtp() {
        return isOtp;
    }

    public void setOtp(Boolean otp) {
        isOtp = otp;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
