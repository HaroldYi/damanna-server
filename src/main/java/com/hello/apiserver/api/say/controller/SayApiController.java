package com.hello.apiserver.api.say.controller;

import com.google.gson.Gson;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String body
    ) throws IOException {

        apiToken = new Gson().fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if (ObjectUtils.isEmpty(body)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                SayVo sayVo = new Gson().fromJson(body, SayVo.class);
                sayVo.setRegDt(new Date());

                sayRepository.save(sayVo);
                return "OK";
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = {"/getSayList/{page}", "/getSayList/{page}/"}, method = RequestMethod.GET)
    public List<SayVo> getSayList (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable("page")int page
    ) throws IOException {

        apiToken = new Gson().fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                PageRequest pr = new PageRequest(page, 15);
                response.setStatus(HttpStatus.OK.value());

                return sayRepository.findAllByUseYn("Y", pr).getContent();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getSayListByUid/{memberId}/{page}", "/getSayListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
    public List<SayVo> getSayListByUid (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable String memberId,
            @PathVariable int page
    ) throws IOException {

        apiToken = new Gson().fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                PageRequest pr = new PageRequest(page, 15);

                return sayRepository.findByMemberIdAndUseYn(memberId, "Y", pr).getContent();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = "/newComment", method = RequestMethod.POST)
    public String newComment (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new Gson();

        apiToken = gson.fromJson(apiToken, String.class);
        CommentVo commentVo = gson.fromJson(body, CommentVo.class);
        MemberVo memberVo = new MemberVo();
        memberVo.setId(commentVo.getMemberId());
        commentVo.setMember(memberVo);

        if(Auth.checkToken(apiToken)) {
            if (body == null || body.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(commentVo.getSayId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(commentVo.getComment())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'comment' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentVo.setRegDt(new Date());
                    commentRepository.save(commentVo);
                    return "OK";
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
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new Gson();

        apiToken = gson.fromJson(apiToken, String.class);
        CommentReplyVo commentReplyVo = gson.fromJson(body, CommentReplyVo.class);

        if(Auth.checkToken(apiToken)) {
            if (body == null || body.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(commentReplyVo.getCommentId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(commentReplyVo.getComment())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'message' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    commentReplyVo.setRegDt(new Date());
                    commentReplyRepository.save(commentReplyVo);
                    return "OK";
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
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable long sayId,
            @PathVariable String memberId,
            @RequestBody(required = false)String body
    ) throws IOException {

        Gson gson = new Gson();

        apiToken = gson.fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if (body == null || body.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(sayId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(memberId);

                    SayVo sayVo = sayRepository.findById(sayId);
                    LikeSayVo likeSayVo = likeSayRepository.findBySayIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    if(likeSayVo != null) {
                        likeSayVo.setUseYn("N");
                        likeSayVo.setUpdateDt(new Date());
                    } else {
                        likeSayVo = new LikeSayVo();
                        likeSayVo.setSayId(sayVo.getId());
                        likeSayVo.setMember(memberVo);
                        likeSayVo.setRegDt(new Date());
                        likeSayVo.setUpdateDt(new Date());
                    }

                    likeSayRepository.save(likeSayVo);

                    return "OK";
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
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable long sayId,
            @RequestBody(required = false)String body
    ) throws IOException {
        Gson gson = new Gson();

        apiToken = gson.fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if(ObjectUtils.isEmpty(sayId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());
                SayVo sayVo = sayRepository.findById(sayId);
                sayRepository.delete(sayVo);

                return "OK";
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }
}
