package com.hello.apiserver.api.point.controller;

import com.google.gson.Gson;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.point.service.PointRepository;
import com.hello.apiserver.api.point.vo.PointVo;
import com.hello.apiserver.api.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value = {"/point", "/point/"})
public class PointApiController {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping(value = "/updatePoint", method = RequestMethod.PUT)
    public String newSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String msg
    ) throws IOException {

        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);

//        if(Auth.checkApiKey(apiKey)) {
            if (msg == null || msg.isEmpty()) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                PointVo pointVo = gson.fromJson(msg, PointVo.class);
                pointVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));

                this.pointRepository.save(pointVo);
                MemberVo memberVo = memberRepository.findById(pointVo.getMemberId());

                if(pointVo.getSource().equals("attendance")) {
                    memberVo.setLastAttendance(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                    this.memberRepository.save(memberVo);
                }

                memberVo.setPoint(memberVo.getPoint() + pointVo.getPoint());
                this.memberRepository.save(memberVo);

                return HttpStatus.OK.toString();
            }
//        } else {
//            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
//        }
//
        return "";
    }
}
