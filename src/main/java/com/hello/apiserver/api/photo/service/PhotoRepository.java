package com.hello.apiserver.api.photo.service;

import com.hello.apiserver.api.photo.vo.PhotoVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends CrudRepository<PhotoVo, String> {
    Page<PhotoVo> findAll(Pageable pageable);

    Page<PhotoVo> findPhotoVoByMemberIdAndUseYn(String memberId, String useYn,Pageable pageable);

    PhotoVo findByIdAndUseYn(String id, String useYn);
}
