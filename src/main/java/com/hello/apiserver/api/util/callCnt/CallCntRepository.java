package com.hello.apiserver.api.util.callCnt;

import org.springframework.data.repository.CrudRepository;

public interface CallCntRepository extends CrudRepository<CallCntVo, Long> {
    @Override
    Iterable<CallCntVo> findAll();
}
