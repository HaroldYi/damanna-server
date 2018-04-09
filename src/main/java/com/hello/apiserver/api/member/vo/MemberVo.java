package com.hello.apiserver.api.member.vo;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "member")
public class MemberVo {
    /* user uid : required */
    @Id
    private String id = "";

    @Column(length = 20, nullable = false)
    /* 이름 : required */
    private String name = "";

    @Column(nullable = false)
    /* 나이 : required */
    private int age;

    @Column(name = "gender_code", nullable = false)
    private String genderCode = "";

//    @OneToOne
//    @JoinColumn(name = "gender_code", referencedColumnName = "gender_code")
//    /* 성별 : required */
//    private GenderVo gender;

    @Column(length = 50, nullable = false, unique = true)
    /* email : required */
    private String email = "";

    @Column(nullable = false)
    /* profileUrl : required */
    private String profileUrl = "";

    @Column
    /* profileUrl_org : optional */
    private String profileUrlOrg = "";

    @Column
    /* profile_file : optional */
    private String profileFile = "";

    @Column
    /* last_signIn : optional */
    private Date lastSignIn;

    @Column(columnDefinition = "POINT")
    /* location : optional */
    private Point location;

    @Column(nullable = false)
    /* clientToken : required */
    private String clientToken = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

//    public GenderVo getGender() {
//        return gender;
//    }
//
//    public void setGender(GenderVo gender) {
//        this.gender = gender;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileUrlOrg() {
        return profileUrlOrg;
    }

    public void setProfileUrlOrg(String profileUrlOrg) {
        this.profileUrlOrg = profileUrlOrg;
    }

    public String getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(String profileFile) {
        this.profileFile = profileFile;
    }

    public Date getLastSignIn() {
        return lastSignIn;
    }

    public void setLastSignIn(Date lastSignIn) {
        this.lastSignIn = lastSignIn;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }
}