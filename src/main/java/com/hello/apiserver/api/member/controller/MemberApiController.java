package com.hello.apiserver.api.member.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.util.Auth;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = {"/member", "/member/"})
public class MemberApiController {

    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping(value = {"/newMember", "/newMember/"}, method = RequestMethod.POST)
    public String newMember (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String userInfo
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(userInfo, MemberVo.class);

        Point point = new GeometryFactory().createPoint(new Coordinate(37.73, 60.45));
        memberVo.setLocation(point);

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(userInfo)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberVo.getId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'id' parameter must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getName())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'name' parameter must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getEmail())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'email' parameter must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getAge())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'age' parameter must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getProfileUrl())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'profileUrl' parameter must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getClientToken())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'clientToken' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    memberRepository.save(memberVo);
                    return "OK";
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/changeNickName/{memberId}", method = RequestMethod.PUT)
    public String changeNickName (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String nickName,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(nickName, MemberVo.class);

        if(!ObjectUtils.isEmpty(apiToken)) {
            if (Auth.checkToken(apiToken)) {

                if (ObjectUtils.isEmpty(nickName)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value());
                } else {
                    if (ObjectUtils.isEmpty(memberVo.getName())) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'name' parameter must not be null or empty");
                    } else {
                        response.setStatus(HttpStatus.OK.value());
                        MemberVo member = memberRepository.findById(memberId);
                        member.setName(memberVo.getName());
                        memberRepository.save(member);
                        return "OK";
                    }
                }
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
            }
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'apiToken' parameter must not be null or empty");
        }

        return "";
    }

    @RequestMapping(value = "/changeAge/{memberId}", method = RequestMethod.PUT)
    public String changeAge (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody(required = false)String age,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        MemberVo memberVo = gson.fromJson(age, MemberVo.class);

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(age)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberVo.getAge())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'age' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    MemberVo member = memberRepository.findById(memberId);
                    member.setAge(memberVo.getAge());
                    memberRepository.save(member);
                    return "OK";
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/getMemberList/{page}", method = RequestMethod.GET)
    public List<MemberVo> getMemberList (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable int page
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(page)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(page)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'page' parameter must not be null or empty");
                } else {

                    PageRequest pr = new PageRequest(page, 15);

                    Page<MemberVo> memberList = memberRepository.findAll(pr);
                    response.setStatus(HttpStatus.OK.value());

                    return memberList.getContent();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getMemberInfo/{memberId}/", "/getMemberInfo/{memberId}"}, method = RequestMethod.GET)
    public MemberVo getMemberList (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable("memberId")String memberId
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(memberId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'age' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    return memberRepository.findById(memberId);
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }
}
