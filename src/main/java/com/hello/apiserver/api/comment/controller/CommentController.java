package com.hello.apiserver.api.comment.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.comment.service.CommentReplyRepository;
import com.hello.apiserver.api.comment.service.CommentRepository;
import com.hello.apiserver.api.comment.vo.CommentReplyVo;
import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.util.Auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = {"/say/newComment", "/meet/newComment"}, method = RequestMethod.POST)
    public String newComment (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
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

                    String sortation = "";
                    if(request.getRequestURI().indexOf("say") != -1) {
                        sortation = "S";
                    } else if(request.getRequestURI().indexOf("meet") != -1) {
                        sortation = "M";
                    }

                    commentVo.setSortation(sortation);

                    return gson.toJson(this.commentRepository.save(commentVo));
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }

    @RequestMapping(value = {"/say/newCommentReply", "/meet/newCommentReply"}, method = RequestMethod.POST)
    public String newCommentReply (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
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
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }
}
