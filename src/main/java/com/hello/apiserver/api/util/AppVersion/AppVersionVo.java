package com.hello.apiserver.api.util.AppVersion;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "app_version")
public class AppVersionVo {
    @Id
    private String id = "";

    private float appVersion;

    private Date regDt;

    private Date updateDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(float appVersion) {
        this.appVersion = appVersion;
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
}
