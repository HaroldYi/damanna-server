package com.hello.apiserver.api.meet.service;

import com.hello.apiserver.api.meet.vo.MeetVo;

import java.util.List;

public interface MeetRepositoryCustom {
    List<MeetVo> getMeetList(int page);
}
