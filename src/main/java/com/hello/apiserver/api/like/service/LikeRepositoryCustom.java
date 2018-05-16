package com.hello.apiserver.api.like.service;

import com.hello.apiserver.api.like.vo.LikeSayVo;

import java.util.List;

public interface LikeRepositoryCustom {
    List<LikeSayVo> findLike(String sayId, String sortation);
}
