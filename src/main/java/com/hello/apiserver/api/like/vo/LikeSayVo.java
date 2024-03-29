package com.hello.apiserver.api.like.vo;

import com.hello.apiserver.api.member.vo.MemberVo;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "like_say")
public class LikeSayVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "say_id", nullable = false)
    private String sayId;

    @Column(name = "meet_id", nullable = false)
    private String meetId;

    @Column(name = "festival_id", nullable = false)
    private String festivalId;

    @ManyToOne(targetEntity=MemberVo.class)
//    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberVo member;

    @Column(nullable = false)
    @Filter(name = "use_yn", condition = "use_yn = 'Y'")
    private String useYn = "Y";

    @Column(nullable = false)
    private Date regDt;

    @Column(nullable = false)
    private Date updateDt;

    @Column
    private String sortation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSayId() {
        return sayId;
    }

    public void setSayId(String sayId) {
        this.sayId = sayId;
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }

    public String getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(String festivalId) {
        this.festivalId = festivalId;
    }

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

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getSortation() {
        return sortation;
    }

    public void setSortation(String sortation) {
        this.sortation = sortation;
    }
}
