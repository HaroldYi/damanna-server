package com.hello.apiserver.api.point.controller;

import com.google.gson.Gson;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.point.service.PointRepository;
import com.hello.apiserver.api.point.vo.PointVo;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.vo.HttpResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity updatePoint (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String msg
    ) throws IOException {

        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        if(Auth.checkApiKey(apiKey)) {
            if (msg == null || msg.isEmpty()) {
                httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {

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

                httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                httpStatus = HttpStatus.OK;
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }
}
