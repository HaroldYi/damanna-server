package com.hello.apiserver.api.util.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "regex")
public class RegexVo {
    @Id
    private String id = "";

    @Column
    private String regex = "";

    @Column
    private Date regDt;

    @Column
    private Date updateDt;

    @Column
    private String regexVersion = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getRegexVersion() {
        return regexVersion;
    }

    public void setRegexVersion(String regexVersion) {
        this.regexVersion = regexVersion;
    }
}
