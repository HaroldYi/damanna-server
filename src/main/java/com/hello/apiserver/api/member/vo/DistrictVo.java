package com.hello.apiserver.api.member.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "district_code")
public class DistrictVo {
    @Id
    @Column(name = "district_code")
    private String districtCode;

    @Column
    private String districtKrName;

    @Column
    private String districtKrNameAbbr;

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictKrName() {
        return districtKrName;
    }

    public void setDistrictKrName(String districtKrName) {
        this.districtKrName = districtKrName;
    }

    public String getDistrictKrNameAbbr() {
        return districtKrNameAbbr;
    }

    public void setDistrictKrNameAbbr(String districtKrNameAbbr) {
        this.districtKrNameAbbr = districtKrNameAbbr;
    }
}
