package com.hello.apiserver.api.member.mapper;

import com.hello.apiserver.api.member.vo.MemberVo;

import java.util.List;

public interface MemberMapper {
    List<MemberVo> findMemberList();
}
