package com.tuhalang.apigw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "API_PERMISSION")
public class ApiPermission implements Serializable {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Id
    @Column(name = "API_NAME")
    private String apiName;

    @Column(name = "HAS_PERMISSION")
    private Boolean hasPermission;

    @Column(name = "THROTTLING")
    private int throttling;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Boolean getHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(Boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    public int getThrottling() {
        return throttling;
    }

    public void setThrottling(int throttling) {
        this.throttling = throttling;
    }
}
