package com.hello.apiserver.api.festival.service;

import com.hello.apiserver.api.festival.vo.FestivalVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface FestivalRepository extends CrudRepository<FestivalVo, Long> {
    Page<FestivalVo> findByEventstartdateAfterAndEventenddateBeforeOrderByEventstartdateAsc(Date startDt, Date endDt, Pageable pageable);
    Page<FestivalVo> findByEventenddateAfterOrderByEventstartdateAsc(Date endDt, Pageable pageable);
    FestivalVo findByContentid(String festivalId);
}