package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.say.vo.CommentReplyVo;
import com.hello.apiserver.api.say.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentReplyRepository extends CrudRepository<CommentReplyVo, Long> {
//    Page<CommentReplyVo> findAllByUseYn(String useYn, Pageable pageable);

//    Page<CommentReplyVo> findByMemberIdAndUseYn(String memberId, String useYn, Pageable pageable);
}
