package com.tuhalang.apigw.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "AG_SERVICE")
public class AgService implements Serializable {

    @Id
    @Column(name = "AG_SERVICE_ID")
    private UUID agServiceId;

    @ManyToOne
    @JoinColumn(name = "AG_APP_ID")
    private AgApp agApp;

    @Column(name = "SERVICE_NAME", nullable = false, length = 255, unique = true)
    private String serviceName;

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

    @OneToMany(mappedBy = "agService", fetch = FetchType.LAZY)
    private List<AgApi> agApis;

    @OneToMany(mappedBy = "agService", fetch = FetchType.LAZY)
    private List<AgRoleApi> agRoleApis;

    public UUID getAgServiceId() {
        return agServiceId;
    }

    public void setAgServiceId(UUID agServiceId) {
        this.agServiceId = agServiceId;
    }

    public AgApp getAgApp() {
        return agApp;
    }

    public void setAgApp(AgApp agApp) {
        this.agApp = agApp;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public List<AgApi> getAgApis() {
        return agApis;
    }

    public void setAgApis(List<AgApi> agApis) {
        this.agApis = agApis;
    }

    public List<AgRoleApi> getAgRoleApis() {
        return agRoleApis;
    }

    public void setAgRoleApis(List<AgRoleApi> agRoleApis) {
        this.agRoleApis = agRoleApis;
    }
}
