package com.hello.apiserver.api.festival.vo;

import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "festival")
public class FestivalVo {

    @Column
    private String addr1 = "";

    @Column
    private String addr2 = "";

    @Column
    private String areacode = "";

    @Column
    private String cat1 = "";

    @Column
    private String cat2 = "";

    @Column
    private String cat3 = "";

    @Id
    private String contentid;

    @Column
    private String contenttypeid = "";

    @Column
    private Date createdtime;

    @Column
    private String eventenddate = "";

    @Column
    private String eventstartdate = "";

    @Column
    private String firstimage = "";

    @Column
    private String firstimage2 = "";

    @Column
    private String mapx = "";

    @Column
    private String mapy = "";

    @Column
    private String mlevel = "";

    @Column
    private String modifiedtime = "";

    @Column
    private String readcount = "";

    @Column
    private String sigungucode = "";

    @Column
    private String tel = "";

    @Column
    private String title = "";

    @OneToMany
    @JoinColumn(name = "festival_id", referencedColumnName = "contentid")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<CommentVo> comment;

    @OneToMany
    @JoinColumn(name = "festival_id", referencedColumnName = "contentid")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<LikeSayVo> likeSay;

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getContenttypeid() {
        return contenttypeid;
    }

    public void setContenttypeid(String contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
    }

    public String getMlevel() {
        return mlevel;
    }

    public void setMlevel(String mlevel) {
        this.mlevel = mlevel;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getReadcount() {
        return readcount;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }

    public String getSigungucode() {
        return sigungucode;
    }

    public void setSigungucode(String sigungucode) {
        this.sigungucode = sigungucode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setLikeSay(List<LikeSayVo> likeSay) {
        this.likeSay = likeSay;
    }
}