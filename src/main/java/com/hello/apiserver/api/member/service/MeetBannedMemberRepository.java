package com.hello.apiserver.api.member.service;

import com.hello.apiserver.api.member.vo.MeetBannedMemberVo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeetBannedMemberRepository extends CrudRepository<MeetBannedMemberVo, Long> {
    List<MeetBannedMemberVo> findByChannelUrl(String channelUrl);
}
