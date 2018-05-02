package com.hello.apiserver.api.say.mapper;

import com.hello.apiserver.api.say.vo.NearSayVo;

import java.util.List;
import java.util.Map;

public interface SayMapper {
    List<NearSayVo> findSayByDistance(Map<String, Object> map);
    List<String> findLikeMemberList(Map<String, Object> map);
}
