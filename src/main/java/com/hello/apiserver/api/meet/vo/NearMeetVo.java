package com.hello.apiserver.api.meet.vo;

import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MeetBannedMemberVo;
import com.hello.apiserver.api.member.vo.MemberVo;

import java.util.Date;
import java.util.List;

public class NearMeetVo {
    private String id;

    private String title = "";

    private String message = "";

    private String channelUrl = "";

    private String memberId = "";

    private Date regDt;

    private MemberVo member;
    private String name = "";

    private List<CommentVo> comment;

    private List<com.hello.apiserver.api.like.vo.LikeSayVo> likeSay;

    private List<MeetBannedMemberVo> meetBannedMemberList;

    private String useYn = "Y";

    private int commentCnt = 0;

    private String clientToken = "";

    private String profileUrl = "";

    private String profileUrlOrg = "";

    private String profileFile = "";

    private String originalImg = "";
    private String thumbnailImg = "";
    private String fileName = "";

    private String sortation = "";

    private Date meetStartDt;
    private Date meetEndDt;
    private String place = "";
    private String locationLat = "";
    private String locationLon = "";
    private String districtKrName = "";
    private String districtKrNameAbbr = "";
    private String memberLimit = "";

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

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
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

    public MemberVo getMember() {
        return member;
    }

    public void setMember(MemberVo member) {
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CommentVo> getComment() {
        return comment;
    }

    public void setComment(List<CommentVo> comment) {
        this.comment = comment;
    }

    public List<LikeSayVo> getLikeSay() {
        return likeSay;
    }

    public void setLikeSay(List<com.hello.apiserver.api.like.vo.LikeSayVo> likeSay) {
        this.likeSay = likeSay;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileUrlOrg() {
        return profileUrlOrg;
    }

    public void setProfileUrlOrg(String profileUrlOrg) {
        this.profileUrlOrg = profileUrlOrg;
    }

    public String getProfileFile() {
        return profileFile;
    }

    public void setProfileFile(String profileFile) {
        this.profileFile = profileFile;
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

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(String locationLon) {
        this.locationLon = locationLon;
    }

    public String getDistrictKrName() {
        return districtKrName;
    }

    public void setDistrictKrName(String districtKrName) {
        this.districtKrName = districtKrName;
    }

    public String getDistrictKrNameAbbr() {
        return districtKrNameAbbr;
    }

    public void setDistrictKrNameAbbr(String districtKrNameAbbr) {
        this.districtKrNameAbbr = districtKrNameAbbr;
    }

    public String getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(String memberLimit) {
        this.memberLimit = memberLimit;
    }

    public List<MeetBannedMemberVo> getMeetBannedMemberList() {
        return meetBannedMemberList;
    }

    public void setMeetBannedMemberList(List<MeetBannedMemberVo> meetBannedMemberList) {
        this.meetBannedMemberList = meetBannedMemberList;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
