package com.tuhalang.apigw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AG_APP")
public class AgApp implements Serializable {

    @Id
    @Column(name = "AG_APP_ID")
    private UUID agAppId;

    @Column(name = "APP_NAME", nullable = false, length = 255)
    private String appName;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

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


    @OneToMany(mappedBy = "agApp", fetch = FetchType.LAZY)
    private List<AgRoleApi> agRoleApis;

    @OneToMany(mappedBy = "agApp", fetch = FetchType.LAZY)
    private List<AgApi> agApis;

    @OneToMany(mappedBy = "agApp", fetch = FetchType.LAZY)
    private List<AgService> agServices;

    public UUID getAgAppId() {
        return agAppId;
    }

    public void setAgAppId(UUID agAppId) {
        this.agAppId = agAppId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<AgRoleApi> getAgRoleApis() {
        return agRoleApis;
    }

    public void setAgRoleApis(List<AgRoleApi> agRoleApis) {
        this.agRoleApis = agRoleApis;
    }

    public List<AgApi> getAgApis() {
        return agApis;
    }

    public void setAgApis(List<AgApi> agApis) {
        this.agApis = agApis;
    }

    public List<AgService> getAgServices() {
        return agServices;
    }

    public void setAgServices(List<AgService> agServices) {
        this.agServices = agServices;
    }
}
