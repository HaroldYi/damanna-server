package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.say.vo.SayVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SayRepository extends CrudRepository<SayVo, Long> {

//    @Query(name = "Say.findSayByDistance", nativeQuery = true)
//    List<SayVo> findSayByDistance(@Param("seLat") double seLat, @Param("seLon") double seLon, @Param("nwLat") double nwLat, @Param("nwLon") double nwLon);
//    List<MeetVo> findAll();

    Page<SayVo> findAllByUseYnOrderByRegDtDesc(String useYn, Pageable pageable);

    Page<SayVo> findByMemberIdAndUseYnOrderByRegDtDesc(String memberId, String useYn, Pageable pageable);

//    Page<MeetVo> findByLocationLatBetweenAndLocationLonBetween(double slat, double elat, double slon, double elon, Pageable pageable);

    SayVo findByIdAndUseYn(String sayId, String useYn);
}