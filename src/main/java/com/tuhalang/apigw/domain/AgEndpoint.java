package com.tuhalang.apigw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "AG_ENDPOINT")
public class AgEndpoint {

    @Id
    @Column(name="AG_ENDPOINT_ID")
    private UUID agEndpointId;

    @Column(name = "ENDPOINT")
    private String endpoint;

    @Column(name = "URL_HEALTHY_CHECK")
    private String urlHealthyCheck;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "LAST_USED")
    private Date lastUsed;

    public UUID getAgEndpointId() {
        return agEndpointId;
    }

    public void setAgEndpointId(UUID agEndpointId) {
        this.agEndpointId = agEndpointId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUrlHealthyCheck() {
        return urlHealthyCheck;
    }

    public void setUrlHealthyCheck(String urlHealthyCheck) {
        this.urlHealthyCheck = urlHealthyCheck;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}
