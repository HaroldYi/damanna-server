package com.hello.apiserver.api.place.mapper;

import com.hello.apiserver.api.place.vo.PlaceVo;

import java.util.List;

public interface PlaceRankMapper {
    List<PlaceVo> getPlaceRank();
}
