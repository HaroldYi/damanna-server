package com.hello.apiserver.api.util.AppVersion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "app_version")
public class AppVersionVo {
    @Id
    private String id = "";

    @Column
    private float android;

    @Column
    private float ios;

    @Column
    private String status;

    @Column
    private Date regDt;

    @Column
    private Date updateDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAndroid() {
        return android;
    }

    public void setAndroid(float android) {
        this.android = android;
    }

    public float getIos() {
        return ios;
    }

    public void setIos(float ios) {
        this.ios = ios;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
