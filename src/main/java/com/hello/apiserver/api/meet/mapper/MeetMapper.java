package com.hello.apiserver.api.meet.mapper;

import com.hello.apiserver.api.meet.vo.MeetVo;
import com.hello.apiserver.api.meet.vo.NearMeetVo;
import com.hello.apiserver.api.say.vo.NearSayVo;

import java.util.List;
import java.util.Map;

public interface MeetMapper {
    List<NearMeetVo> findMeetByDistance(Map<String, Object> map);
    List<NearMeetVo> getMeetListByUid(Map<String, Object> map);
}
