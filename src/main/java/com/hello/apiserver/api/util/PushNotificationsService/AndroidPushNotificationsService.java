package com.hello.apiserver.api.util.PushNotificationsService;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.hello.apiserver.api.util.HttpRequest.HeaderRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAA51RpBMY:APA91bFn9W1LIVLMEzqRXxlPGCimN5EmxELSlvOJ7XVqhVZA1OVs2fafzpSwcJ5zODSSPNWhGJlasGb5axyZv4XgvzTZIDAQ3lbt8FhyPH9P0I2_dK2dgSrSl8wIyTGQ6blQ4YY8DixP";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        interceptors.add(new HeaderRequestInterceptor("project_id", "993553614022"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}