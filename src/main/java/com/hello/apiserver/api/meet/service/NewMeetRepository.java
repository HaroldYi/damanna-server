package com.hello.apiserver.api.meet.service;

import com.hello.apiserver.api.meet.vo.NewMeetVo;
import org.springframework.data.repository.CrudRepository;

public interface NewMeetRepository extends CrudRepository<NewMeetVo, Long> {

}