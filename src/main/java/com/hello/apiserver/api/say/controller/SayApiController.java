package com.hello.apiserver.api.say.controller;

import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.mapper.SayMapper;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.say.vo.NearSayVo;
import com.hello.apiserver.api.say.vo.SayVo;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.PushNotificationsService.AndroidPushNotificationsService;
import com.hello.apiserver.api.util.commonVo.HttpResponseVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = {"/say", "/say/"})
public class SayApiController {

    @Autowired
    private SayMapper sayMapper;

    @Autowired
    private SayRepository sayRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AndroidPushNotificationsService androidPushNotificationsService;

    private HttpResponseVo httpResponseVo = new HttpResponseVo();
    private HttpStatus httpStatus;

    @RequestMapping(value = "/newSay", method = RequestMethod.POST)
    public ResponseEntity newSay (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                this.httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {

                SayVo sayVo = new Gson().fromJson(body, SayVo.class);
                sayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                sayVo.setUseYn("Y");

                sayVo = this.sayRepository.save(sayVo);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("to", "/topics/notice");
                jsonBody.put("priority", "high");
                jsonBody.put("content_available", true);

                JSONObject data = new JSONObject();
                data.put("sayId", sayVo.getId());
                data.put("notiMsg", sayVo.getMessage());
                data.put("sortation", "S");
                data.put("content_available", true);

//        body.put("notification", notification);
                jsonBody.put("data", data);

                HttpHeaders headers = new HttpHeaders();
                Charset utf8 = Charset.forName("UTF-8");
                MediaType mediaType = new MediaType("application", "json", utf8);
                headers.setContentType(mediaType);

                HttpEntity<String> pushRequest = new HttpEntity<>(jsonBody.toString(), headers);

                CompletableFuture<String> pushNotification = androidPushNotificationsService.send(pushRequest);
                CompletableFuture.allOf(pushNotification).join();

                try {
                    String firebaseResponse = pushNotification.get();
                    this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    this.httpStatus = HttpStatus.OK;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    this.httpResponseVo.setHttpResponse("", HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

                } catch (ExecutionException e) {
                    e.printStackTrace();
                    this.httpResponseVo.setHttpResponse("", HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = {"/getSay/{sayId}", "/getSay/{sayId}/"}, method = RequestMethod.GET)
    public ResponseEntity getSay (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("sayId")String sayId
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        SayVo sayVo = new SayVo();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
                isError = true;
                this.httpResponseVo.setHttpResponse("The 'msg' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                isError = false;

                sayVo = this.sayRepository.findByIdAndUseYn(sayId, "Y");
                List<LikeSayVo> likeSayVoList = this.likeRepository.findBySayIdAndSortation(sayId, "S");

                sayVo.setLikeSay(likeSayVoList);

                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(sayVo));
        }
    }

    @RequestMapping(value = {"/getSayList/{page}", "/getSayList/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity getSayList (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("page")int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        List<SayVo> sayVoList = new ArrayList<>();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
                isError = true;
                this.httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                PageRequest pr = new PageRequest(page, 20);
                sayVoList = this.sayRepository.findAllByUseYnOrderByRegDtDesc("Y", pr).getContent();

                isError = false;
                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(sayVoList));
        }
    }

    @RequestMapping(value = {"/getNearSayList", "/getNearSayList/"}, method = RequestMethod.GET)
    public ResponseEntity getNearSayList (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distanceMetres") int distanceMetres,
            @RequestParam(value = "page") int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        List<NearSayVo> sayVoList = new ArrayList<>();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(latitude)) {
                this.httpResponseVo.setHttpResponse("The 'latitude' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else if (ObjectUtils.isEmpty(longitude)) {
                this.httpResponseVo.setHttpResponse("The 'longitude' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else if (ObjectUtils.isEmpty(distanceMetres)) {
                this.httpResponseVo.setHttpResponse("The 'distanceMetres' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else if (ObjectUtils.isEmpty(page)) {
                this.httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {

                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;

                page *= 20;

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                PageRequest pr = new PageRequest(page, 20);
                Map<String, Object> map = new HashMap<>();

                if(distanceMetres < 300) {
                    distanceMetres *= (1.414 * 1000);

                    WGS84Point startPoint = new WGS84Point(latitude, longitude);

                    WGS84Point nw = VincentyGeodesy.moveInDirection(startPoint, 300, distanceMetres);

                    WGS84Point se = VincentyGeodesy.moveInDirection(startPoint, 120, distanceMetres);

                    map.put("seLat", se.getLatitude());
                    map.put("seLon", se.getLongitude());
                    map.put("nwLat", nw.getLatitude());
                    map.put("nwLon", nw.getLongitude());
                    map.put("page", page);

//                    sayVoList = this.sayRepository.findByLocationLatBetweenAndLocationLonBetween(se.getLatitude(), nw.getLatitude(), nw.getLongitude(), se.getLongitude(), pr).getContent();
                } else {
//                    map.put("seLat", se.getLatitude());
//                    map.put("seLon", se.getLongitude());
//                    map.put("nwLat", nw.getLatitude());
//                    map.put("nwLon", nw.getLongitude());
                    map.put("page", page);
                }

                sayVoList = this.sayMapper.findSayByDistance(map);

                int i = 0;
                for(NearSayVo sayVo : sayVoList) {

                    map.put("sayId", sayVo.getId());
                    map.put("sortation", "S");

                    List<LikeSayVo> likeSayVoList = this.likeRepository.findBySayIdAndSortation(sayVo.getId(), "S");

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(sayVo.getMemberId());
                    memberVo.setName(sayVo.getName());
                    memberVo.setClientToken(sayVo.getClientToken());
                    memberVo.setProfileUrl(sayVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(sayVo.getProfileUrlOrg());
                    memberVo.setProfileFile(sayVo.getProfileFile());

                    sayVo.setMember(memberVo);
                    sayVo.setLikeSay(likeSayVoList);
                    sayVoList.set(i++, sayVo);
                }
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(sayVoList));
        }
    }

    @RequestMapping(value = {"/getSayListByUid/{memberId}/{page}", "/getSayListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity getSayListByUid (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String memberId,
            @PathVariable int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        List<NearSayVo> sayVoList = new ArrayList<>();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
                isError = true;
                this.httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                isError = false;
                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;

//                PageRequest pr = new PageRequest(page, 20);
//
                Map<String, Object> map = new HashMap<>();
                map.put("memberId", memberId);
                map.put("page", page);

                sayVoList = this.sayMapper.getSayListByUid(map);
                for(NearSayVo sayVo : sayVoList) {

                    map.put("sayId", sayVo.getId());
                    map.put("sortation", "S");

                    List<String> likeSayVoListStr = this.sayMapper.findLikeMemberList(map);
                    List<LikeSayVo> likeSayVoList = new ArrayList<>();
                    for(String like : likeSayVoListStr) {
                        LikeSayVo likeSayVo = new LikeSayVo();
                        MemberVo memberVo = new MemberVo();
                        memberVo.setId(like);

                        likeSayVo.setMember(memberVo);
                        likeSayVoList.add(likeSayVo);
                    }

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(sayVo.getMemberId());
                    memberVo.setName(sayVo.getName());
                    memberVo.setClientToken(sayVo.getClientToken());
                    memberVo.setProfileUrl(sayVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(sayVo.getProfileUrlOrg());
                    memberVo.setProfileFile(sayVo.getProfileFile());

                    sayVo.setMember(memberVo);
                    sayVo.setLikeSay(likeSayVoList);
                }
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(sayVoList));
        }
    }

    @RequestMapping(value = "/deleteSay/{sayId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSay (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String sayId
    ) throws IOException {
        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);
        boolean isError = false;

        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(sayId)) {
                this.httpResponseVo.setHttpResponse("The 'sayId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;

                SayVo sayVo = this.sayRepository.findByIdAndUseYn(sayId, "Y");
                if(sayVo != null) {
//                    sayVo.setUseYn("N");
//                    this.sayRepository.save(sayVo);
                    this.sayRepository.delete(sayVo);
                }

                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }
}
