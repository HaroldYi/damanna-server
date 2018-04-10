package com.hello.apiserver.api.say.vo;

import com.hello.apiserver.api.member.vo.MemberVo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "like_say")
public class LikeSayVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(nullable = false)
//    private String sayId = "";

    @ManyToOne(targetEntity=MemberVo.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberVo member;

    @Column(nullable = false)
    private String useYn = "Y";

    @Column(nullable = false)
    private Date regDt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //    public String getSayId() {
//        return sayId;
//    }
//
//    public void setSayId(String sayId) {
//        this.sayId = sayId;
//    }


    public MemberVo getMember() {
        return member;
    }

    public void setMember(MemberVo member) {
        this.member = member;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
}
