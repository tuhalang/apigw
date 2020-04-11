package com.tuhalang.apigw.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "AG_LOG")
public class AgLog implements Serializable {

    @Id
    @Column(name = "AG_LOG_ID")
    private String agLogId;

    @Column(name = "ERROR_CODE")
    private String errorCode;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "PROCESS_TIME")
    private Long processTime;

    @Column(name = "IP")
    private String ip;

    @Column(name = "API_NAME")
    private String apiName;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "REQUEST", length = 100000)
    private String request;

    @Column(name = "RESPONSE", length = 100000)
    private String response;


    public String getAgLogId() {
        return agLogId;
    }

    public void setAgLogId(String agLogId) {
        this.agLogId = agLogId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
