package com.hello.apiserver.api.festival.vo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "festival_meet")
public class FestivalMeetVo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id = "";

    @Column(name = "festival_id", nullable = false)
    private String festivalId = "";

    @Column
    private String channelUrl = "";

    @Column
    private Date meetDt;

    @Column
    private Date regDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFestivalId() {
        return festivalId;
    }

    public void setFestivalId(String festivalId) {
        this.festivalId = festivalId;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public Date getMeetDt() {
        return meetDt;
    }

    public void setMeetDt(Date meetDt) {
        this.meetDt = meetDt;
    }

    public Date getRegDt() {
        return regDt;
    }

    public void setRegDt(Date regDt) {
        this.regDt = regDt;
    }
}
