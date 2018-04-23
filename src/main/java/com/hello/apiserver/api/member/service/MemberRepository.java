package com.hello.apiserver.api.member.service;

import com.hello.apiserver.api.member.vo.MemberVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<MemberVo, Long> {

    Page<MemberVo> findAllByOrderByLastSignInDesc(Pageable pageable);

    MemberVo findById(String id);

    Page<MemberVo> findByLocationHashContaining(String hash, Pageable pageable);

    Page<MemberVo> findByLocationLatBetweenAndLocationLonBetweenAndIdNot(double slat, double elat, double slon, double elon, String memberId, Pageable pageable);
}
