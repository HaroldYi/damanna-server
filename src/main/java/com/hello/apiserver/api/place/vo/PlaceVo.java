package com.hello.apiserver.api.place.vo;

public class PlaceVo {
    private String placeId;

    private String placeName;

    private int rank;

    private int beforeRank;

    private int variation;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getBeforeRank() {
        return beforeRank;
    }

    public void setBeforeRank(int beforeRank) {
        this.beforeRank = beforeRank;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }
}
