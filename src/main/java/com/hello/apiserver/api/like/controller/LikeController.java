package com.hello.apiserver.api.like.controller;

import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.meet.service.MeetRepository;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.PushNotificationsService.AndroidPushNotificationsService;
import com.hello.apiserver.api.util.vo.HttpResponseVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class LikeController {

    @Autowired
    private SayRepository sayRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @RequestMapping(value = {"/meet/likeMeet/{sayId}/{memberId}/{clientToken}", "/say/likeSay/{sayId}/{memberId}/{clientToken}", "/festival/likeFestival/{sayId}/{memberId}/{clientToken}"}, method = RequestMethod.PUT)
    public ResponseEntity likeSay (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String sayId,
            @PathVariable String memberId,
            @PathVariable String clientToken
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
                httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if(ObjectUtils.isEmpty(sayId)) {
                    httpResponseVo.setHttpResponse("The 'sayId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;

                    String sortation = "";
                    LikeSayVo likeSayVo = new LikeSayVo();
                    MemberVo memberVo = this.memberRepository.findById(memberId);

                    if(request.getRequestURI().indexOf("say") != -1) {
                        likeSayVo = this.likeRepository.findBySayIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    } else if(request.getRequestURI().indexOf("meet") != -1) {
                        likeSayVo = this.likeRepository.findByMeetIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    } else if(request.getRequestURI().indexOf("festival") != -1) {
                        likeSayVo = this.likeRepository.findByFestivalIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    }

                    if(likeSayVo != null) {
                        this.likeRepository.delete(likeSayVo.getId());
                    } else {
                        likeSayVo = new LikeSayVo();

                        if(request.getRequestURI().indexOf("say") != -1) {
                            sortation = "S";
                            likeSayVo.setSayId(sayId);
                        } else if(request.getRequestURI().indexOf("meet") != -1) {
                            sortation = "M";
                            likeSayVo.setMeetId(sayId);
                        } else if(request.getRequestURI().indexOf("festival") != -1) {
                            sortation = "F";
                            likeSayVo.setFestivalId(sayId);
                        }

                        likeSayVo.setMember(memberVo);
                        likeSayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setUpdateDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setSortation(sortation);

                        this.likeRepository.save(likeSayVo);

                        JSONObject body = new JSONObject();
                        body.put("to", clientToken);
                        body.put("priority", "high");

                        String nofiMsg = String.format("%s님이 이글을 좋아합니다.", memberVo.getName());

                        JSONObject data = new JSONObject();
                        data.put("sayId", sayId);
                        data.put("nofiMsg", nofiMsg);
                        data.put("sortation", sortation);

                        body.put("data", data);

                        HttpHeaders headers = new HttpHeaders();
                        Charset utf8 = Charset.forName("UTF-8");
                        MediaType mediaType = new MediaType("application", "json", utf8);
                        headers.setContentType(mediaType);

                        HttpEntity<String> httpEntity = new HttpEntity<>(body.toString(), headers);

                        CompletableFuture<String> pushNotification = this.androidPushNotificationsService.send(httpEntity);
                        CompletableFuture.allOf(pushNotification).join();

                        try {
                            String firebaseResponse = pushNotification.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }

    @RequestMapping(value = {"/meet/likeMeet/{sayId}/{memberId}", "/say/likeSay/{sayId}/{memberId}", "/festival/likeFestival/{sayId}/{memberId}"}, method = RequestMethod.PUT)
    public ResponseEntity likeSay1 (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String sayId,
            @PathVariable String memberId
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
                httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if(ObjectUtils.isEmpty(sayId)) {
                    httpResponseVo.setHttpResponse("The 'sayId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;

                    String sortation = "";
                    LikeSayVo likeSayVo = new LikeSayVo();
                    MemberVo memberVo = this.memberRepository.findById(memberId);

                    if(request.getRequestURI().indexOf("say") != -1) {
                        likeSayVo = this.likeRepository.findBySayIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    } else if(request.getRequestURI().indexOf("meet") != -1) {
                        likeSayVo = this.likeRepository.findByMeetIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    } else if(request.getRequestURI().indexOf("festival") != -1) {
                        likeSayVo = this.likeRepository.findByFestivalIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    }

                    if(likeSayVo != null) {
                        this.likeRepository.delete(likeSayVo.getId());
                    } else {
                        likeSayVo = new LikeSayVo();

                        if(request.getRequestURI().indexOf("say") != -1) {
                            sortation = "S";
                            likeSayVo.setSayId(sayId);
                        } else if(request.getRequestURI().indexOf("meet") != -1) {
                            sortation = "M";
                            likeSayVo.setMeetId(sayId);
                        } else if(request.getRequestURI().indexOf("festival") != -1) {
                            sortation = "F";
                            likeSayVo.setFestivalId(sayId);
                        }

                        likeSayVo.setMember(memberVo);
                        likeSayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setUpdateDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setSortation(sortation);

                        this.likeRepository.save(likeSayVo);
                    }
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }
}
