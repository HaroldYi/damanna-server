package com.hello.apiserver.api.member.service;

import com.hello.apiserver.api.member.vo.MemberVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<MemberVo, Long> {

    Page<MemberVo> findByIdNotAndAndUseYnOrderByLastSignInDesc(String memberId, String useYn, Pageable pageable);

    MemberVo findById(String id);

    Page<MemberVo> findByLocationHashContaining(String hash, Pageable pageable);

    Page<MemberVo> findByLocationLatBetweenAndLocationLonBetweenAndIdNotOrderByLastSignInDesc(double slat, double elat, double slon, double elon, String memberId, Pageable pageable);
}
