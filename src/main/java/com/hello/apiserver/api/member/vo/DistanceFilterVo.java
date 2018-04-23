package com.hello.apiserver.api.member.vo;

public class DistanceFilterVo {
    private String memberId = "";
    private double latitude;
    private double longitude;
    private int page;
    private double distanceMetres;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public double getDistanceMetres() {
        return distanceMetres;
    }

    public void setDistanceMetres(double distanceMetres) {
        this.distanceMetres = distanceMetres;
    }
}
