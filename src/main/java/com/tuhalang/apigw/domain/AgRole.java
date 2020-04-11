package com.tuhalang.apigw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AG_ROLE")
public class AgRole implements Serializable {

    @Id
    @Column(name = "AG_ROLE_ID")
    private UUID agRoleId;

    @Column(name = "ROLE_NAME", nullable = false, length = 255, unique = true)
    private String roleName;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000)
    private String description;

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "ACCEPT_METHOD", nullable = false)
    private Integer acceptMethod;

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

    @OneToMany(mappedBy = "agRole", fetch = FetchType.LAZY)
    private List<AgRoleApi> agRoleApis;

    public UUID getAgRoleId() {
        return agRoleId;
    }

    public void setAgRoleId(UUID agRoleId) {
        this.agRoleId = agRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Integer getAcceptMethod() {
        return acceptMethod;
    }

    public void setAcceptMethod(Integer acceptMethod) {
        this.acceptMethod = acceptMethod;
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