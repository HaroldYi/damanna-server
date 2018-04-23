package com.hello.apiserver.api.say.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.service.CommentReplyRepository;
import com.hello.apiserver.api.say.service.CommentRepository;
import com.hello.apiserver.api.say.service.LikeSayRepository;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.say.vo.CommentReplyVo;
import com.hello.apiserver.api.say.vo.CommentVo;
import com.hello.apiserver.api.say.vo.LikeSayVo;
import com.hello.apiserver.api.say.vo.SayVo;
import com.hello.apiserver.api.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value = {"/say", "/say/"})
public class SayApiController {

    @Autowired
    private SayRepository sayRepository;

    @Autowired
    private LikeSayRepository likeSayRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping(value = "/newSay", method = RequestMethod.POST)
    public String newSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                SayVo sayVo = new Gson().fromJson(body, SayVo.class);
                sayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                sayVo.setUseYn("Y");
                this.sayRepository.save(sayVo);
                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = {"/getSay/{sayId}", "/getSay/{sayId}/"}, method = RequestMethod.GET)
    public String getSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable("sayId")String sayId
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                return gson.toJson(this.sayRepository.findByIdAndUseYn(sayId, "Y"));
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getSayList/{page}", "/getSayList/{page}/"}, method = RequestMethod.GET)
    public String getSayList (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable("page")int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                PageRequest pr = new PageRequest(page, 15);
                response.setStatus(HttpStatus.OK.value());

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                return gson.toJson(this.sayRepository.findAllByUseYnOrderByRegDtDesc("Y", pr).getContent());
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getSayListByUid/{memberId}/{page}", "/getSayListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
    public String getSayListByUid (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable String memberId,
            @PathVariable int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                PageRequest pr = new PageRequest(page, 15);

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                return gson.toJson(this.sayRepository.findByMemberIdAndUseYnOrderByRegDtDesc(memberId, "Y", pr).getContent());
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = "/newComment", method = RequestMethod.POST)
    public String newComment (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiKey = gson.fromJson(apiKey, String.class);
        CommentVo commentVo = gson.fromJson(body, CommentVo.class);
        MemberVo memberVo = new MemberVo();
        memberVo.setId(commentVo.getMemberId());
        commentVo.setMember(memberVo);

        if(Auth.checkApiKey(apiKey)) {
            if (body == null || body.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(commentVo.getSayId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(commentVo.getComment())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'comment' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));

                    return gson.toJson(this.commentRepository.save(commentVo));
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/newCommentReply", method = RequestMethod.POST)
    public String newCommentReply (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);
        CommentReplyVo commentReplyVo = gson.fromJson(body, CommentReplyVo.class);

        if(Auth.checkApiKey(apiKey)) {
            if (body == null || body.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(commentReplyVo.getCommentId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(commentReplyVo.getComment())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'message' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentReplyVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                    this.commentReplyRepository.save(commentReplyVo);
                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/likeSay/{sayId}/{memberId}", method = RequestMethod.PUT)
    public String likeSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable String sayId,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(sayId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(memberId);

                    SayVo sayVo = this.sayRepository.findByIdAndUseYn(sayId, "Y");
                    LikeSayVo likeSayVo = this.likeSayRepository.findBySayIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    if(likeSayVo != null) {
//                        likeSayVo.setUseYn("N");
//                        likeSayVo.setUpdateDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).getTimeInMillis()));
                        likeSayRepository.delete(likeSayVo);
                    } else {
                        likeSayVo = new LikeSayVo();
                        likeSayVo.setSayId(sayVo.getId());
                        likeSayVo.setMember(memberVo);
                        likeSayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setUpdateDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        this.likeSayRepository.save(likeSayVo);

//                        HttpClient httpclient = new DefaultHttpClient();
//                        org.apache.http.client.methods.HttpPost httppost = new HttpPost("https://us-central1-noryangjin-18dfb.cloudfunctions.net/sendPushMsg");
//
//                        try {
//                            // 아래처럼 적절히 응용해서 데이터형식을 넣으시고
//                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs[0], "utf-8"));
//
//                            //HTTP Post 요청 실행
//                            HttpResponse response = httpclient.execute(httppost);
//                        } catch (ClientProtocolException e) {
//                            Crashlytics.logException(e);
//                        } catch (IOException e) {
//                            Crashlytics.logException(e);
//                        }
                    }

//                    likeSayRepository.save(likeSayVo);

                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/deleteSay/{sayId}", method = RequestMethod.DELETE)
    public String deleteSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable String sayId
    ) throws IOException {
        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(sayId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                SayVo sayVo = this.sayRepository.findByIdAndUseYn(sayId, "Y");
                if(sayVo != null) {
                    sayVo.setUseYn("N");
                    this.sayRepository.save(sayVo);
                }

                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }
}
