package com.hello.apiserver.api.say.vo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "commentReply")
public class CommentReplyVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String comment = "";

    @Column(name = "comment_id", nullable = false)
    private String commentId = "";

    @Column(nullable = false)
    private Date regDt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
}
