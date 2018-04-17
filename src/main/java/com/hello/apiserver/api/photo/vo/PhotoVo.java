package com.hello.apiserver.api.photo.vo;

import com.hello.apiserver.api.member.vo.MemberVo;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "photo")
@DynamicUpdate
public class PhotoVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id = "";

    @Column(nullable = false)
    private String fileName = "";

//    @Column(nullable = false, length = 28)
//    private String memberId = "";

    @ManyToOne(targetEntity=MemberVo.class)
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MemberVo member;

    @Column(nullable = false)
    private String originalImg = "";

    @Column(nullable = true)
    private String thumbnailImg = "";

    @Column(nullable = false)
    private Date regDt;

    @Column(nullable = false)
    private String useYn = "Y";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

//    public String getMemberId() {
//        return memberId;
//    }
//
//    public void setMemberId(String memberId) {
//        this.memberId = memberId;
//    }

    public String getOriginalImg() {
        return originalImg;
    }

    public void setOriginalImg(String originalImg) {
        this.originalImg = originalImg;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
}