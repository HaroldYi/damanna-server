package com.hello.apiserver.api.util;

import org.springframework.util.ObjectUtils;

public class Auth {
    public static boolean checkToken(String token) {
        if(ObjectUtils.isEmpty(token)) {
            return false;
        } else {
            // TODO Api token 체크
            return true;
        }
    }
}
