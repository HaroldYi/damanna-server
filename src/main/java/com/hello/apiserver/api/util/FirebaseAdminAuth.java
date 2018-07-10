package com.hello.apiserver.api.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hello.apiserver.api.util.HttpRequest.HeaderRequestInterceptor;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = {"/auth", "/auth/"})
public class FirebaseAdminAuth {

    // Kakao API request url to retrieve user profile based on access token
    private static final String requestMeUrl = "https://kapi.kakao.com/v1/user/me?secure_resource=true";

    private FirebaseApp defaultApp;

    @RequestMapping(value = {"verifyToken", "verifyToken/"}, method = RequestMethod.POST)
    public ResponseEntity checkAndroidAppVersion(@RequestBody(required = false) String requestBody) {

        FileInputStream serviceAccount = null;
        FirebaseOptions options = null;

        try {
            serviceAccount = new FileInputStream(new PathMatchingResourcePatternResolver().getResource("classpath:/firebase/jejumate-e540b-firebase-adminsdk-q10ta-163789a2d2.json").getFile().getAbsolutePath());
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.defaultApp = FirebaseApp.initializeApp(options);

        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = new Gson().fromJson(requestBody, mapType);
        map.put("firebase_token", createFirebaseToken(map.get("token")));

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON_UTF8).body(map);
    }

    private String createFirebaseToken(String kakaoAccessToken) {
        try {
            String firebaseResponse = requestMe(kakaoAccessToken).get();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseMap = new Gson().fromJson(firebaseResponse, mapType);
            Map<String, Object> memberInfoMap = (Map<String, Object>) responseMap.get("properties");

            double userId = (double) responseMap.get("id");
            String nickname = (String) memberInfoMap.get("nickname");
            String profileImage = (String) memberInfoMap.get("profile_image");

            UserRecord userRecord = this.updateOrCreateUser(userId, nickname, profileImage);
            return FirebaseAuth.getInstance(this.defaultApp).createCustomToken(userRecord.getUid());

            // {"id":810116557,"properties":{"profile_image":"https://k.kakaocdn.net/dn/cZiHF6/btqnc2CAdCX/3KbGWkIVkVJ3Rbd8TuONb0/profile_640x640s.jpg","nickname":"Harold J. Yi","thumbnail_image":"https://k.kakaocdn.net/dn/cZiHF6/btqnc2CAdCX/3KbGWkIVkVJ3Rbd8TuONb0/profile_110x110c.jpg"}}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        return "";
    }

    private CompletableFuture<String> requestMe(String kakaoAccessToken) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", String.format("Bearer %s", kakaoAccessToken)));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.getForObject(requestMeUrl, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

    private UserRecord updateOrCreateUser(double userId, String displayName, String photoURL) throws FirebaseAuthException {
        String uid = String.format("KAKAO:%d", (int) userId);

        try {
            UserRecord userRecord = FirebaseAuth.getInstance(this.defaultApp).getUser(uid);
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(userRecord.getUid())
                    .setDisplayName(displayName)
                    .setPhotoUrl(photoURL)
                    .setDisabled(false);

            return FirebaseAuth.getInstance(this.defaultApp).updateUser(request);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();

            String errorCode = e.getErrorCode();
            if(errorCode.equals("user-not-found")) {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setUid(uid)
                        .setDisplayName(displayName)
                        .setPhotoUrl(photoURL)
                        .setDisabled(false);

                return FirebaseAuth.getInstance(this.defaultApp).createUser(request);
            }

            return null;
        }
    }
}
