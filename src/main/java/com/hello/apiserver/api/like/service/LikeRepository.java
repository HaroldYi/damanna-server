package com.hello.apiserver.api.like.service;

import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeRepository extends CrudRepository<LikeSayVo, Long>, LikeRepositoryCustom {

    LikeSayVo findBySayIdAndMemberAndUseYn(String sayId, MemberVo memberVo, String useYn);

    List<LikeSayVo> findBySayIdAndSortation(String sayId, String sortation);
}