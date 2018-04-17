package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.vo.LikeSayVo;
import com.hello.apiserver.api.say.vo.SayVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface LikeSayRepository extends CrudRepository<LikeSayVo, Long> {
    LikeSayVo findBySayIdAndMemberAndUseYn(String sayId, MemberVo memberVo, String useYn);
}
