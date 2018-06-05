package com.hello.apiserver.api.push.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hello.apiserver.api.util.PushNotificationsService.AndroidPushNotificationsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/push")
public class PushController {

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @RequestMapping(value = "/sendPushMsg", method = RequestMethod.POST)
    public ResponseEntity<String> sendPushMsg(
            @RequestBody(required = false)String requestBody
    ) {

        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = new Gson().fromJson(requestBody, mapType);

        JSONObject body = new JSONObject();
        body.put("to", map.get("clientToken"));
        body.put("priority", "high");
//        body.put("content_available", true);

//        JSONObject notification = new JSONObject();
//        notification.put("title", "제주메이트");
//        notification.put("body", map.get("nofiMsg"));

        JSONObject data = new JSONObject();
        data.put("sayId", map.get("sayId"));
        data.put("nofiMsg", map.get("nofiMsg"));
        data.put("sortation", map.get("sortation"));

//        body.put("notification", notification);
        body.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        Charset utf8 = Charset.forName("UTF-8");
        MediaType mediaType = new MediaType("application", "json", utf8);
        headers.setContentType(mediaType);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
