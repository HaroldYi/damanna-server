package com.hello.apiserver.api.util;

import org.springframework.util.ObjectUtils;

public class Auth {
    public static boolean checkApiKey(String apiKey) {
        if(ObjectUtils.isEmpty(apiKey)) {
            return false;
        } else {
            // TODO Api apiKey 체크
            return true;
        }
    }
}
