package com.hello.apiserver.api.say.vo;

import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "say")
public class SayVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, length = 1000)
    private String message = "";

//    @Column(nullable = false, columnDefinition = "POINT")
//    private Point location;

    @Column(name = "member_id", nullable = false, length = 28)
    private String memberId = "";

    @Column(nullable = false)
    private Date regDt;

    @ManyToOne(targetEntity=MemberVo.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MemberVo member;

    @OneToMany
    @JoinColumn(name = "say_id", referencedColumnName = "id")
    @Cascade(CascadeType.REMOVE)
    private List<CommentVo> comment;

    @OneToMany
    @JoinColumn(name = "say_id", referencedColumnName = "id")
    @Cascade(CascadeType.REMOVE)
    private List<LikeSayVo> likeSay;

    @Column(nullable = false)
    private String useYn = "Y";

    private String originalImg = "";
    private String thumbnailImg = "";
    private String fileName = "";

    public MemberVo getMember() {
        return member;
    }

    public void setMember(MemberVo member) {
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public Point getLocation() {
//        return location;
//    }
//
//    public void setLocation(Point location) {
//        this.location = location;
//    }

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

    public List<CommentVo> getComment() {
        return comment;
    }

    public void setComment(List<CommentVo> comment) {
        this.comment = comment;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public List<LikeSayVo> getLikeSay() {
        return likeSay;
    }

    public void setLikeSay(List<LikeSayVo> likeSay) {
        this.likeSay = likeSay;
    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
