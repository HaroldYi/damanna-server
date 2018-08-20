package com.hello.apiserver.api.comment.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.comment.service.CommentReplyRepository;
import com.hello.apiserver.api.comment.service.CommentRepository;
import com.hello.apiserver.api.comment.vo.CommentReplyVo;
import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.vo.HttpResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentReplyRepository commentReplyRepository;

    @RequestMapping(value = {"/say/newComment", "/meet/newComment", "/festival/newComment"}, method = RequestMethod.POST)
    public ResponseEntity newComment (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        boolean isError = false;

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

//        apiKey = gson.fromJson(apiKey, String.class);
        CommentVo commentVo = gson.fromJson(body, CommentVo.class);
        MemberVo memberVo = new MemberVo();
        memberVo.setId(commentVo.getMemberId());
        commentVo.setMember(memberVo);

        if(Auth.checkApiKey(apiKey)) {
            if (body == null || body.isEmpty()) {
                httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {
                if(ObjectUtils.isEmpty(commentVo.getSayId()) && ObjectUtils.isEmpty(commentVo.getMeetId()) && ObjectUtils.isEmpty(commentVo.getFestivalId())) {
                    httpResponseVo.setHttpResponse("The 'sayId', 'meetId' or 'festivalId' request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else if(ObjectUtils.isEmpty(commentVo.getComment())) {
                    httpResponseVo.setHttpResponse("The 'comment' request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));

                    String sortation = "";
                    if(request.getRequestURI().indexOf("say") != -1) {
                        sortation = "S";
                        commentVo.setMeetId(null);
                        commentVo.setFestivalId(null);
                    } else if(request.getRequestURI().indexOf("meet") != -1) {
                        sortation = "M";
                        commentVo.setSayId(null);
                        commentVo.setFestivalId(null);
                    } else if(request.getRequestURI().indexOf("festival") != -1) {
                        sortation = "F";
                        commentVo.setSayId(null);
                        commentVo.setMeetId(null);
                    }

                    commentVo.setSortation(sortation);
                    commentVo = this.commentRepository.save(commentVo);

                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                }
            }
        } else {
            isError = true;
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(httpStatus).body(httpResponseVo);
        } else {
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(commentVo));
        }
    }

    @RequestMapping(value = {"/say/newCommentReply", "/meet/newCommentReply", "/festival/newCommentReply"}, method = RequestMethod.POST)
    public ResponseEntity newCommentReply (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new Gson();

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

//        apiKey = gson.fromJson(apiKey, String.class);
        CommentReplyVo commentReplyVo = gson.fromJson(body, CommentReplyVo.class);

        if(Auth.checkApiKey(apiKey)) {
            if (body == null || body.isEmpty()) {
                httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if(ObjectUtils.isEmpty(commentReplyVo.getCommentId())) {
                    httpResponseVo.setHttpResponse("The 'memberId' request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(commentReplyVo.getComment())) {
                    httpResponseVo.setHttpResponse("The 'message' request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentReplyVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                    this.commentReplyRepository.save(commentReplyVo);

                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }
}
