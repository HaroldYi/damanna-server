package com.hello.apiserver.api.comment.service;

import com.hello.apiserver.api.comment.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentVo, Long> {
    Page<CommentVo> findAllByUseYn(String useYn, Pageable pageable);

    Page<CommentVo> findByMemberIdAndUseYn(String memberId, String useYn, Pageable pageable);

    List<CommentVo> findBySayId(String sayId);
}
