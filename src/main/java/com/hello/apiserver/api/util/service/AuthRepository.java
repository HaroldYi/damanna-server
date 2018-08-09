package com.hello.apiserver.api.util.service;

import com.hello.apiserver.api.util.vo.ApikeyVo;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<ApikeyVo, Long> {
    ApikeyVo findByApiKeyAndUseYn(String apiKey, String useYn);
}