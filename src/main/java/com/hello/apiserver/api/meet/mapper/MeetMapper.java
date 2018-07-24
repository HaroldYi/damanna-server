package com.hello.apiserver.api.meet.mapper;

import com.hello.apiserver.api.meet.vo.NearMeetVo;

import java.util.List;
import java.util.Map;

public interface MeetMapper {
    List<NearMeetVo> findMeetByDistance(Map<String, Object> map);
    List<NearMeetVo> getMeetListByUid(Map<String, Object> map);
    List<String> findLikeMemberList(Map<String, Object> map);
    int checkPassword(Map<String, Object> map);
}