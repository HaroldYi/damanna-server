package com.hello.apiserver.api.admin.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/sendPushMsg", method = RequestMethod.POST)
    public String sendPushMsg(
            @RequestBody(required = false)String body
    ) {

        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = new Gson().fromJson(body, mapType);

        // This registration token comes from the client FCM SDKs.
        String registrationToken = map.get("clientToken");

        // See documentation on defining a message payload.
        Message.Builder messageBuilder = Message.builder()
                .putData("sayId", map.get("sayId"))
                .putData("nofiMsg", map.get("nofiMsg"));
        if(!ObjectUtils.isEmpty(map.get("sortation"))) {
            messageBuilder.putData("sortation", map.get("sortation"));
        }

        Message message = messageBuilder
//                .setNotification(new Notification("Title", "Body"))
//                .setAndroidConfig(AndroidConfig.builder()
//                        .setRestrictedPackageName("com.hello.jejumate")
//                        .build())
//                .setApnsConfig(ApnsConfig.builder()
//                        .setAps(Aps.builder()
//                                .setAlert(ApsAlert.builder()
//                                        .setTitle("Title")
//                                        .setBody("Body")
//                                        .build())
//                                .build())
//                        .build())
//                .setWebpushConfig(WebpushConfig.builder()
//                        .putHeader("X-Custom-Val", "Foo")
//                        .setNotification(new WebpushNotification("Title", "Body"))
//                        .build())
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.

        try {
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            return HttpStatus.OK.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR.toString();
        }
    }
}
