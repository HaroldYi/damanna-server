package com.hello.apiserver.api.util.service;

import com.hello.apiserver.api.util.vo.AppVersionVo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AppVersionRepository extends CrudRepository<AppVersionVo, Long> {
    AppVersionVo findById(@Param("id") String id);
}
