package com.tuhalang.apigw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AG_THROTTLING")
public class AgThrottling implements Serializable {

    @Id
    @Column(name = "AG_THROTTLING_ID")
    private UUID agThrottlingId;

    @Column(name = "THROTTLING_NAME", nullable = false, length = 255, unique = true)
    private String throttlingName;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "NUM_OF_REQUEST", nullable = false)
    private Long numOfRequest;

    @Column(name = "TIME_SECOND", nullable = false)
    private Long timeSecond;

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

    @OneToMany(mappedBy = "agThrottling", fetch = FetchType.LAZY)
    private List<AgRoleApi> agRoleApis;

    public UUID getAgThrottlingId() {
        return agThrottlingId;
    }

    public void setAgThrottlingId(UUID agThrottlingId) {
        this.agThrottlingId = agThrottlingId;
    }

    public String getThrottlingName() {
        return throttlingName;
    }

    public void setThrottlingName(String throttlingName) {
        this.throttlingName = throttlingName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getNumOfRequest() {
        return numOfRequest;
    }

    public void setNumOfRequest(Long numOfRequest) {
        this.numOfRequest = numOfRequest;
    }

    public Long getTimeSecond() {
        return timeSecond;
    }

    public void setTimeSecond(Long timeSecond) {
        this.timeSecond = timeSecond;
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
}
