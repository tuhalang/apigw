package com.tuhalang.apigw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AG_USER")
public class AgUser implements Serializable {

    @Id
    @Column(name = "AG_USER_ID")
    private UUID agUserId;

    @Column(name = "USERNAME", nullable = false, length = 255, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 500)
    private String password;

    @Column(name = "SALT", nullable = false, length = 1000)
    private String salt;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

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


    @OneToMany(mappedBy = "agUser", fetch = FetchType.LAZY)
    private List<AgRoleApi> agRoleApis;

    public UUID getAgUserId() {
        return agUserId;
    }

    public void setAgUserId(UUID agUserId) {
        this.agUserId = agUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
