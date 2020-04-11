package com.tuhalang.apigw.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "AG_API")
public class AgApi implements Serializable {

    @Id
    @Column(name = "AG_API_ID")
    private UUID agApiId;

    @Column(name = "API_NAME", nullable = false, length = 1000, unique = true)
    private String apiName;

    @Column(name = "API_KEY", nullable = false, length = 1000)
    private String apiKey;

    @ManyToOne
    @JoinColumn(name = "AG_SERVICE_ID")
    private AgService agService;

    @ManyToOne
    @JoinColumn(name = "AG_APP_ID")
    private AgApp agApp;

    @Column(name = "ENDPOINT", nullable = false, length = 1000)
    private String endpoint;

    @Column(name = "RESOURCE", nullable = false, length = 500)
    private String resource;

    @Column(name = "METHOD", nullable = false)
    private Integer method;

    @Column(name = "TIMEOUT", nullable = false)
    private Long timeout;

    @Column(name = "VERSION", nullable = false)
    private String version;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "IS_AUTH", nullable = false)
    private Boolean isAuth;

    @Column(name = "IS_OTP", nullable = false)
    private Boolean isOtp;

    @Column(name = "TYPE", nullable = false, length = 50)
    private String type;

    @Column(name = "CREATED_DATE", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public UUID getAgApiId() {
        return agApiId;
    }

    public void setAgApiId(UUID agApiId) {
        this.agApiId = agApiId;
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

    public AgService getAgService() {
        return agService;
    }

    public void setAgService(AgService agService) {
        this.agService = agService;
    }

    public AgApp getAgApp() {
        return agApp;
    }

    public void setAgApp(AgApp agApp) {
        this.agApp = agApp;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAuth() {
        return isAuth;
    }

    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    public Boolean getOtp() {
        return isOtp;
    }

    public void setOtp(Boolean otp) {
        isOtp = otp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
