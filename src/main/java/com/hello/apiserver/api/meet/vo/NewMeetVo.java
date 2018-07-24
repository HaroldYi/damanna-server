package com.hello.apiserver.api.meet.vo;

import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MeetBannedMemberVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meet")
public class NewMeetVo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String title = "";

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
    @JoinColumn(name = "meet_id", referencedColumnName = "id")
    @Cascade(CascadeType.REMOVE)
    private List<CommentVo> comment;

    @OneToMany
    @JoinColumn(name = "meet_id", referencedColumnName = "id")
    @Cascade(CascadeType.REMOVE)
    private List<LikeSayVo> likeSay;

    @Column(nullable = false)
    private String useYn = "Y";

    @Column(name = "channel_url")
    private String channelUrl = "";

    @Column
    private String sortation = "";

    @Column
    private Date meetStartDt;

    @Column
    private Date meetEndDt;

    @Column
    private String place;

    @Column
    private String memberLimit;

    @OneToMany
    @JoinColumn(name = "channel_url", referencedColumnName = "channel_url", insertable = false, updatable = false)
    @Cascade(CascadeType.REMOVE)
    private List<MeetBannedMemberVo> meetBannedMemberList;

    @Column
    private String originalImg = "";

    @Column
    private String thumbnailImg = "";

    @Column
    private String fileName = "";

    @Column
    private String password = "";

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getSortation() {
        return sortation;
    }

    public void setSortation(String sortation) {
        this.sortation = sortation;
    }

    public Date getMeetStartDt() {
        return meetStartDt;
    }

    public void setMeetStartDt(Date meetStartDt) {
        this.meetStartDt = meetStartDt;
    }

    public Date getMeetEndDt() {
        return meetEndDt;
    }

    public void setMeetEndDt(Date meetEndDt) {
        this.meetEndDt = meetEndDt;
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

    public String getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(String memberLimit) {
        this.memberLimit = memberLimit;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<MeetBannedMemberVo> getMeetBannedMemberList() {
        return meetBannedMemberList;
    }

    public void setMeetBannedMemberList(List<MeetBannedMemberVo> meetBannedMemberList) {
        this.meetBannedMemberList = meetBannedMemberList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
