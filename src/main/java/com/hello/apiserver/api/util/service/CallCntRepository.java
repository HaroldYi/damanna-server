package com.hello.apiserver.api.util.service;

import com.hello.apiserver.api.util.vo.CallCntVo;
import org.springframework.data.repository.CrudRepository;

public interface CallCntRepository extends CrudRepository<CallCntVo, Long> {
    @Override
    Iterable<CallCntVo> findAll();
}
