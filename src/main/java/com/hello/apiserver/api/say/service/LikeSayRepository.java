package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import org.springframework.data.repository.CrudRepository;

public interface LikeSayRepository extends CrudRepository<LikeSayVo, Long> {
    LikeSayVo findBySayIdAndMemberAndUseYn(String sayId, MemberVo memberVo, String useYn);
}
