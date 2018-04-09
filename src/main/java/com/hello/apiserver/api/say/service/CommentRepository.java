package com.hello.apiserver.api.say.service;

import com.hello.apiserver.api.say.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<CommentVo, Long> {
    Page<CommentVo> findAllByUseYn(String useYn, Pageable pageable);

    Page<CommentVo> findByMemberIdAndUseYn(String memberId, String useYn, Pageable pageable);
}
