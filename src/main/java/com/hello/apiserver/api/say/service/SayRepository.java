package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.say.vo.SayVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface SayRepository extends CrudRepository<SayVo, Long> {
    Page<SayVo> findAllByUseYnOrderByRegDtDesc(String useYn, Pageable pageable);

    Page<SayVo> findByMemberIdAndUseYnOrderByRegDtDesc(String memberId, String useYn, Pageable pageable);

    SayVo findByIdAndUseYn(String sayId, String useYn);
}
