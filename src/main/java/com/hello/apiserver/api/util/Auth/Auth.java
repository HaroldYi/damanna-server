package com.hello.apiserver.api.util.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class Auth {

    static AuthRepository authRepository;

    @Autowired(required=true)
    public void setAuthRepository(AuthRepository authRepository) {
        Auth.authRepository = authRepository;
    }

    public static boolean checkApiKey(String apiKey) {
        if(ObjectUtils.isEmpty(apiKey)) {
            return false;
        } else {
            ApikeyVo apikeyVo = authRepository.findByApiKeyAndUseYn(apiKey, "Y");
            if(apikeyVo == null) {
                return false;
            } else {
                return true;
            }
        }
    }
}
