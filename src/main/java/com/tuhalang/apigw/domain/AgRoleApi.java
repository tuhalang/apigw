package com.tuhalang.apigw.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "AG_ROLE_API")
public class AgRoleApi implements Serializable {

    @Id
    @Column(name = "AG_ROLE_API_ID")
    private UUID agRoleApiId;

    @ManyToOne
    @JoinColumn(name = "AG_USER_ID")
    private AgUser agUser;

    @ManyToOne
    @JoinColumn(name = "AG_API_ID")
    private AgApi agApi;

    @ManyToOne
    @JoinColumn(name = "AG_SERVICE_ID")
    private AgService agService;

    @ManyToOne
    @JoinColumn(name = "AG_APP_ID")
    private AgApp agApp;

    @ManyToOne
    @JoinColumn(name = "AG_ROLE_ID")
    private AgRole agRole;

    @ManyToOne
    @JoinColumn(name = "AG_THROTTLING_ID")
    private AgThrottling agThrottling;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

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

    public UUID getAgRoleApiId() {
        return agRoleApiId;
    }

    public void setAgRoleApiId(UUID agRoleApiId) {
        this.agRoleApiId = agRoleApiId;
    }

    public AgUser getAgUser() {
        return agUser;
    }

    public void setAgUser(AgUser agUser) {
        this.agUser = agUser;
    }

    public AgApi getAgApi() {
        return agApi;
    }

    public void setAgApi(AgApi agApi) {
        this.agApi = agApi;
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

    public AgRole getAgRole() {
        return agRole;
    }

    public void setAgRole(AgRole agRole) {
        this.agRole = agRole;
    }

    public AgThrottling getAgThrottling() {
        return agThrottling;
    }

    public void setAgThrottling(AgThrottling agThrottling) {
        this.agThrottling = agThrottling;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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