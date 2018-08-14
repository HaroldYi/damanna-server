package com.hello.apiserver.api.festival.service;

import com.hello.apiserver.api.festival.vo.FestivalMeetVo;
import org.springframework.data.repository.CrudRepository;

public interface FestivalMeetRepository extends CrudRepository<FestivalMeetVo, Long> {

}
