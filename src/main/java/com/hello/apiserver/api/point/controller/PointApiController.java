package com.hello.apiserver.api.point.controller;

import com.google.gson.Gson;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.point.service.PointRepository;
import com.hello.apiserver.api.point.vo.PointVo;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {"/point", "/point/"})
public class PointApiController {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping(value = "/updatePoint", method = RequestMethod.POST)
    public String newSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String msg
    ) throws IOException {

        Gson gson = new Gson();

        apiToken = gson.fromJson(apiToken, String.class);

        if(Auth.checkToken(apiToken)) {
            if (msg == null || msg.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                PointVo pointVo = gson.fromJson(msg, PointVo.class);
                pointVo.setRegDt(new Date());

                pointRepository.save(pointVo);
                if(pointVo.getSource().equals("attendance")) {
                    MemberVo memberVo = memberRepository.findById(pointVo.getMemberId());
                    memberVo.setLastAttendance(new Date());
                    memberRepository.save(memberVo);
                }

                return "OK";
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }
}
