package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.say.vo.SayVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SayRepository extends CrudRepository<SayVo, Long> {

//    @Query(name = "Say.findAll", nativeQuery = true)
//    List<SayVo> findAll(@Param("useYn") String useYn);
//    List<SayVo> findAll();

    Page<SayVo> findAllByUseYnOrderByRegDtDesc(String useYn, Pageable pageable);

    Page<SayVo> findByMemberIdAndUseYnOrderByRegDtDesc(String memberId, String useYn, Pageable pageable);

//    Page<SayVo> findByLocationLatBetweenAndLocationLonBetween(double slat, double elat, double slon, double elon, Pageable pageable);

    SayVo findByIdAndUseYn(String sayId, String useYn);
}