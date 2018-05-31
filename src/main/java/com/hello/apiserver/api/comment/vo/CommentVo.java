package com.hello.apiserver.api.comment.vo;

import com.hello.apiserver.api.member.vo.MemberVo;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
public class CommentVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "say_id")
    private String sayId;

    @Column(name = "meet_id")
    private String meetId;

    @Column(nullable = false)
    private String comment = "";

    @Column(name = "member_id", nullable = false, insertable = false, updatable = false)
    private String memberId = "";

    @ManyToOne(targetEntity=MemberVo.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private MemberVo member;

    @Column
    private Date regDt;

    @OneToMany
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<CommentReplyVo> commentReply;

    @Column
    private String useYn = "Y";

    @Column
    private String sortation = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSayId() {
        return sayId;
    }

    public void setSayId(String sayId) {
        this.sayId = sayId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    public List<CommentReplyVo> getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(List<CommentReplyVo> commentReply) {
        this.commentReply = commentReply;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public String getSortation() {
        return sortation;
    }

    public void setSortation(String sortation) {
        this.sortation = sortation;
    }

    public String getMeetId() {
        return meetId;
    }

    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }
}
