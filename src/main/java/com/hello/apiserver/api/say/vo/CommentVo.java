package com.hello.apiserver.api.say.vo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
public class CommentVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "say_id", nullable = false)
    private String sayId = "";

    @Column(nullable = false)
    private String comment = "";

    @Column(nullable = false, length = 28)
    private String memberId = "";

    @Column
    private Date regDt;

    @OneToMany
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private List<CommentReplyVo> commentReply;

    @Column
    private String useYn = "Y";

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
}
