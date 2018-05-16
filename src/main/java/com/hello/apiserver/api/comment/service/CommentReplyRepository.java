package com.hello.apiserver.api.comment.service;

import com.hello.apiserver.api.comment.vo.CommentReplyVo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentReplyRepository extends CrudRepository<CommentReplyVo, Long> {
//    Page<CommentReplyVo> findAllByUseYn(String useYn, Pageable pageable);

//    Page<CommentReplyVo> findByMemberIdAndUseYn(String memberId, String useYn, Pageable pageable);

    List<CommentReplyVo> findByCommentId(String commentId);
}
