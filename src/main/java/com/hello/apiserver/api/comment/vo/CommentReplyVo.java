package com.hello.apiserver.api.comment.vo;

import com.hello.apiserver.api.member.vo.MemberVo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commentReply")
public class CommentReplyVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String comment = "";

    @Column(name = "comment_id", nullable = false)
    private String commentId = "";

    @Column(name = "member_id")
    private String memberId = "";

    @ManyToOne(targetEntity=MemberVo.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MemberVo member;

    @Column(nullable = false)
    private Date regDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
}
