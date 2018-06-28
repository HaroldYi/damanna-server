package com.hello.apiserver.api.push.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.PushNotificationsService.AndroidPushNotificationsService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @RequestMapping(value = "/sendPushMsg/{os}", method = RequestMethod.POST)
    public ResponseEntity<String> sendPushMsg(
            @RequestBody(required = false)String requestBody,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable(value = "os") String os,
            HttpServletResponse response
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> map = new Gson().fromJson(requestBody, mapType);

            JSONObject body = new JSONObject();
            body.put("to", map.get("clientToken"));
            body.put("priority", "high");
            body.put("content_available", true);

//            JSONObject apns = new JSONObject();
//            apns.put("title", "제주메이트");
//            apns.put("badge", 1);
//            apns.put("sound", "default");
//
//            body.put("apns", apns);


//            if(!ObjectUtils.isEmpty(os) && os.equals("ios")) {
//                JSONObject notification = new JSONObject();
//                notification.put("title", "제주메이트");
//                notification.put("body", map.get("notiMsg"));
//                notification.put("content_available", true);
//                notification.put("sound", "enabled");
//
//                body.put("notification", notification);
//            }

//            JSONObject android = new JSONObject();
//            android.put("ttl", "86400s");
//
//            JSONObject notification1 = new JSONObject();
//            notification1.put("click_action", "OPEN_ACTIVITY_1");
//            android.put("notification", notification1);
//
//            body.put("android", android);

            JSONObject data = new JSONObject();
            data.put("sayId", map.get("sayId"));
            data.put("notiMsg", map.get("notiMsg"));
            data.put("sortation", map.get("sortation"));
            data.put("content_available", true);

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
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
