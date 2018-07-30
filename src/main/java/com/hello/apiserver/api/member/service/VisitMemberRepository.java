package com.hello.apiserver.api.member.service;

import com.hello.apiserver.api.member.vo.VisitMemberVo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VisitMemberRepository extends CrudRepository<VisitMemberVo, Long> {
    VisitMemberVo findByMemberIdAndVisitorMemberId(String memberId, String visitorMemberId);

    List<VisitMemberVo> findByMemberId(String memberId);
}
