package com.hello.apiserver.api.member.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "gender")
public class GenderVo implements Serializable {

    @Id
    private long id;

    @Column(name = "gender_code")
    private String genderCode = "";

    @Column
    private String genderEn = "";

    @Column
    private String genderKr = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderEn() {
        return genderEn;
    }

    public void setGenderEn(String genderEn) {
        this.genderEn = genderEn;
    }

    public String getGenderKr() {
        return genderKr;
    }

    public void setGenderKr(String genderKr) {
        this.genderKr = genderKr;
    }
}
