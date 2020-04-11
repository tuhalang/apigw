package com.tuhalang.apigw.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "AG_ERROR_CODE")
public class AgErrorCode implements Serializable {

    @Id
    @Column(name = "NAME_ERROR")
    private String nameError;

    @Column(name = "ERROR_CODE")
    private String errorCode;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
