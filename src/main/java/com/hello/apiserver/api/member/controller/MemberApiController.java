package com.hello.apiserver.api.member.controller;

import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.DistanceFilterVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.say.vo.SayVo;
import com.hello.apiserver.api.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping(value = {"/member", "/member/"})
public class MemberApiController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SayRepository sayRepository;

    @RequestMapping(value = {"/newMember", "/newMember/"}, method = RequestMethod.POST)
    public String newMember (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String userInfo
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(userInfo, MemberVo.class);
        memberVo.setLastSignIn(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));

//        GeoHash geohash = GeoHash.withCharacterPrecision(memberVo.getLocationLat(), memberVo.getLocationLon(),12);
//        String geohashString = geohash.toBase32();
//
//        memberVo.setLocationHash(geohashString);

        if(Auth.checkApiKey(apiKey)) {

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
                    this.memberRepository.save(memberVo);

                    SayVo sayVo = new SayVo();
                    sayVo.setMessage(String.format("%s님이 가입하셨습니다.", memberVo.getName()));
                    sayVo.setMemberId(memberVo.getId());
                    sayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                    sayVo.setUseYn("Y");

                    this.sayRepository.save(sayVo);

                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/updateMemberInfo/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public String updateMemberInfo (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String args,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(args, MemberVo.class);

        if(!ObjectUtils.isEmpty(apiKey)) {
            if (Auth.checkApiKey(apiKey)) {

                if (ObjectUtils.isEmpty(args)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value());
                } else {
                    MemberVo newMemberVo = this.memberRepository.findById(memberId);
                    if(newMemberVo != null) {
                        newMemberVo.setLastSignIn(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        newMemberVo.setClientToken(memberVo.getClientToken());
                        newMemberVo.setLocationLon(memberVo.getLocationLon());
                        newMemberVo.setLocationLat(memberVo.getLocationLat());

//                    GeoHash geohash = GeoHash.withCharacterPrecision(memberVo.getLocationLat(), memberVo.getLocationLon(),12);
//                    String geohashString = geohash.toBase32();
//
//                    newMemberVo.setLocationHash(geohashString);

                        this.memberRepository.save(newMemberVo);
                    }
                    return HttpStatus.OK.toString();
                }
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
            }
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'apiKey' parameter must not be null or empty");
        }

        return "";
    }

    @RequestMapping(value = "/changeNickName/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public String changeNickName (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String nickName,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(nickName, MemberVo.class);

        if(!ObjectUtils.isEmpty(apiKey)) {
            if (Auth.checkApiKey(apiKey)) {

                if (ObjectUtils.isEmpty(nickName)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value());
                } else {
                    if (ObjectUtils.isEmpty(memberVo.getName())) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'name' parameter must not be null or empty");
                    } else {
                        response.setStatus(HttpStatus.OK.value());
                        MemberVo member = this.memberRepository.findById(memberId);
                        member.setName(memberVo.getName());
                        this.memberRepository.save(member);
                        return HttpStatus.OK.toString();
                    }
                }
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
            }
        } else {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'apiKey' parameter must not be null or empty");
        }

        return "";
    }

    @RequestMapping(value = "/changeAge/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public String changeAge (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @RequestBody(required = false)String age,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        MemberVo memberVo = gson.fromJson(age, MemberVo.class);

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(age)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberVo.getAge())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'age' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    MemberVo member = this.memberRepository.findById(memberId);
                    member.setAge(memberVo.getAge());
                    this.memberRepository.save(member);
                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = "/getMemberList/{page}", method = RequestMethod.GET)
    public String getMemberList (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable int page
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(page)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(page)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'page' parameter must not be null or empty");
                } else {

                    PageRequest pr = new PageRequest(page, 15);

                    Page<MemberVo> memberList = this.memberRepository.findAllByOrderByLastSignInDesc(pr);
                    response.setStatus(HttpStatus.OK.value());

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    return gson.toJson(memberList.getContent());
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getMemberInfo/{memberId}/", "/getMemberInfo/{memberId}"}, method = RequestMethod.GET)
    public String getMemberInfo (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey")String apiKey,
            @PathVariable("memberId")String memberId
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(memberId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    MemberVo memberVo = this.memberRepository.findById(memberId);
//                    GeoHashCircleQuery d = new GeoHashCircleQuery(new WGS84Point(33.5002516, 126.5298658), 15000);
//                    List<GeoHash> geoHashes = d.getSearchHashes();
//
//                    WGS84Point test1 = new WGS84Point(33.4909935, 126.90762);
//                    boolean test = d.contains(test1);

//                    String geohash = GeoHashExtensions.encode(33.5002516, 126.5298658);
//                    Map adjacentAreas = GeoHashExtensions.getAllAdjacentAreasMap(geohash);
//
//                    double [] decoded = GeoHashExtensions.decode(geohash);
//                    List aa = GeoHashExtensions.getAllAdjacentAreasList(geohash);

                    return new Gson().toJson(memberVo);
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/getNearMemberList/{memberId}", "/getNearMemberList/{memberId}/"}, method = RequestMethod.GET)
    public String getNearMemberList (
            HttpServletResponse response,
            @PathVariable(value = "memberId") String memberId,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distanceMetres") int distanceMetres,
            @RequestParam(value = "page") int page,
            @RequestHeader(value = "apiKey") String apiKey
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {
            response.setStatus(HttpStatus.OK.value());

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            if (ObjectUtils.isEmpty(memberId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(latitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'latitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(longitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'longitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(distanceMetres)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'distanceMetres' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(page)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'page' parameter must not be null or empty");
            } else {

                distanceMetres *= (1.414 * 1000);

                PageRequest pr = new PageRequest(page, 15);

                WGS84Point startPoint = new WGS84Point(latitude, longitude);

                WGS84Point nw = VincentyGeodesy.moveInDirection(startPoint, 300, distanceMetres);

                WGS84Point se = VincentyGeodesy.moveInDirection(startPoint, 120, distanceMetres);

                List<MemberVo> memberVoList = this.memberRepository.findByLocationLatBetweenAndLocationLonBetweenAndIdNot(se.getLatitude(), nw.getLatitude(), nw.getLongitude(), se.getLongitude(),memberId, pr).getContent();

                return gson.toJson(memberVoList);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }
}
