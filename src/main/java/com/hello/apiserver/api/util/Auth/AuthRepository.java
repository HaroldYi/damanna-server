package com.hello.apiserver.api.util.Auth;

import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<ApikeyVo, Long> {
    ApikeyVo findByApiKeyAndUseYn(String apiKey, String useYn);
}