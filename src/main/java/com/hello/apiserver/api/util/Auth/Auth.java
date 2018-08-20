package com.hello.apiserver.api.util.Auth;

import com.hello.apiserver.api.util.service.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Auth {

    static AuthRepository authRepository;

    @Autowired(required=true)
    public void setAuthRepository(AuthRepository authRepository) {
        Auth.authRepository = authRepository;
    }

    public static boolean checkApiKey(String apiKey) {
        // TODO 임시.
        return true;

//        if(ObjectUtils.isEmpty(apiKey)) {
//            return false;
//        } else {
//            ApikeyVo apikeyVo = authRepository.findByApiKeyAndUseYn(apiKey, "Y");
//            if(apikeyVo == null) {
//                return false;
//            } else {
//                return true;
//            }
//        }
    }
}
