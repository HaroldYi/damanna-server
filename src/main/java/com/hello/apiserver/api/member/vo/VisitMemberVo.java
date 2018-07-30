package com.hello.apiserver.api.member.vo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "visitor_member")
public class VisitMemberVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "member_id")
    private String memberId = "";

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MemberVo member;

    @Column(name = "visitor_member_id")
    private String visitorMemberId = "";

    @OneToOne
    @JoinColumn(name = "visitor_member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MemberVo visitorMember;

    @Column
    private Date lastVisitDt;

    @Column
    private Date regDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public MemberVo getMember() {
        return member;
    }

    public void setMember(MemberVo member) {
        this.member = member;
    }

    public String getVisitorMemberId() {
        return visitorMemberId;
    }

    public void setVisitorMemberId(String visitorMemberId) {
        this.visitorMemberId = visitorMemberId;
    }

    public MemberVo getVisitorMember() {
        return visitorMember;
    }

    public void setVisitorMember(MemberVo visitorMember) {
        this.visitorMember = visitorMember;
    }

    public Date getLastVisitDt() {
        return lastVisitDt;
    }

    public void setLastVisitDt(Date lastVisitDt) {
        this.lastVisitDt = lastVisitDt;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
}
