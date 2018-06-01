package com.hello.apiserver.api.like.controller;

import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.meet.service.MeetRepository;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.service.SayRepository;
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
public class LikeController {

    @Autowired
    private SayRepository sayRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @RequestMapping(value = {"/meet/likeMeet/{sayId}/{memberId}", "/say/likeSay/{sayId}/{memberId}"}, method = RequestMethod.PUT)
    public String likeSay (
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String sayId,
            @PathVariable String memberId
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {
                if(ObjectUtils.isEmpty(sayId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    String sortation = "";
                    LikeSayVo likeSayVo = new LikeSayVo();
                    MemberVo memberVo = this.memberRepository.findById(memberId);

                    if(request.getRequestURI().indexOf("say") != -1) {
                        likeSayVo = this.likeRepository.findBySayIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    } else if(request.getRequestURI().indexOf("meet") != -1) {
                        likeSayVo = this.likeRepository.findByMeetIdAndMemberAndUseYn(sayId, memberVo, "Y");
                    }

                    if(likeSayVo != null) {
                        likeRepository.delete(likeSayVo.getId());
                    } else {
                        likeSayVo = new LikeSayVo();

                        if(request.getRequestURI().indexOf("say") != -1) {
                            sortation = "S";
                            likeSayVo.setSayId(sayId);
                        } else if(request.getRequestURI().indexOf("meet") != -1) {
                            sortation = "M";
                            likeSayVo.setMeetId(sayId);
                        }

                        likeSayVo.setMember(memberVo);
                        likeSayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setUpdateDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        likeSayVo.setSortation(sortation);

                        this.likeRepository.save(likeSayVo);
                    }

                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }
}
