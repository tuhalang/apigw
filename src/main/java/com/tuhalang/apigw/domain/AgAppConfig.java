package com.tuhalang.apigw.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "AG_APP_CONFIG")
public class AgAppConfig implements Serializable {

    @Id
    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
