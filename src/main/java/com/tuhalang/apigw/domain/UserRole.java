package com.tuhalang.apigw.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE")
public class UserRole implements Serializable {

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @OneToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
