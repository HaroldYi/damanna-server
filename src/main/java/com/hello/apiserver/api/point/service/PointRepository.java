package com.hello.apiserver.api.point.service;

import com.hello.apiserver.api.point.vo.PointVo;
import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<PointVo, Long> {
//    Page<CommentReplyVo> findAllByUseYn(String useYn, Pageable pageable);

//    Page<CommentReplyVo> findByMemberIdAndUseYn(String memberId, String useYn, Pageable pageable);
}
