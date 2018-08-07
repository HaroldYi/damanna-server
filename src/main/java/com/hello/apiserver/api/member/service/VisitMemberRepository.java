package com.hello.apiserver.api.member.service;

import com.hello.apiserver.api.member.vo.VisitMemberVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface VisitMemberRepository extends CrudRepository<VisitMemberVo, Long> {
    VisitMemberVo findByMemberIdAndVisitorMemberId(String memberId, String visitorMemberId);

    Page<VisitMemberVo> findByMemberId(String memberId, Pageable pageable);
}
