package com.hello.apiserver.api.member.controller;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.mapper.MemberMapper;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.commonVo.HttpResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = {"/member", "/member/"})
public class MemberApiController {

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SayRepository sayRepository;

    @RequestMapping(value = {"/newMember", "/newMember/"}, method = RequestMethod.POST)
    public ResponseEntity newMember (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String userInfo
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(userInfo, MemberVo.class);
        memberVo.setLastSignIn(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
        memberVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
        memberVo.setUseYn("Y");
        memberVo.setBlockYn("N");
        memberVo.setKind("M");

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        GeoHash geohash = GeoHash.withCharacterPrecision(memberVo.getLocationLat(), memberVo.getLocationLon(),12);
        String geohashString = geohash.toBase32();

        memberVo.setLocationHash(geohashString);

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(userInfo)) {
                httpResponseVo.setHttpResponse("The 'name' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if(ObjectUtils.isEmpty(memberVo.getId())) {
                    httpResponseVo.setHttpResponse("The 'id' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(memberVo.getName())) {
                    httpResponseVo.setHttpResponse("The 'name' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(memberVo.getAge())) {
                    httpResponseVo.setHttpResponse("The 'age' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(memberVo.getProfileUrl())) {
                    httpResponseVo.setHttpResponse("The 'profileUrl' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(memberVo.getClientToken())) {
                    httpResponseVo.setHttpResponse("The 'clientToken' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                    this.memberRepository.save(memberVo);

//                    SayVo sayVo = new SayVo();
//                    sayVo.setMessage(String.format("%s님이 가입하셨습니다.", memberVo.getName()));
//                    sayVo.setMemberId(memberVo.getId());
//                    sayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
//                    sayVo.setUseYn("Y");
//
//                    this.sayRepository.save(sayVo);
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }

    @RequestMapping(value = "/updateMemberInfo/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public ResponseEntity updateMemberInfo (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String args,
            @PathVariable String memberId
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(args, MemberVo.class);

//        if(!ObjectUtils.isEmpty(apiKey)) {
            if (Auth.checkApiKey(apiKey)) {

                if (ObjectUtils.isEmpty(args)) {
                    httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    MemberVo newMemberVo = this.memberRepository.findById(memberId);
                    if(newMemberVo != null) {
                        newMemberVo.setLastSignIn(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                        newMemberVo.setClientToken(memberVo.getClientToken());
                        newMemberVo.setLocationLon(memberVo.getLocationLon());
                        newMemberVo.setLocationLat(memberVo.getLocationLat());

                        GeoHash geohash = GeoHash.withCharacterPrecision(memberVo.getLocationLat(), memberVo.getLocationLon(),12);
                        String geohashString = geohash.toBase32();

                        newMemberVo.setLocationHash(geohashString);

                        this.memberRepository.save(newMemberVo);
                    }

                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                }
            } else {
                httpStatus = HttpStatus.UNAUTHORIZED;
                httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
//        } else {
//            response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'apiKey' parameter must not be null or empty");
//        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }

    @RequestMapping(value = "/changeNickName/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public ResponseEntity changeNickName (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String nickName,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

//        apiToken = gson.fromJson(apiToken, String.class);
        MemberVo memberVo = gson.fromJson(nickName, MemberVo.class);

        if (Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(memberVo.getName())) {
                httpResponseVo.setHttpResponse("The 'name' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                MemberVo member = this.memberRepository.findById(memberId);
                member.setName(memberVo.getName());
                this.memberRepository.save(member);

                httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());

                httpStatus = HttpStatus.OK;
            }
        } else {
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            httpStatus = HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }

    @RequestMapping(value = "/changeAge/{memberId}", method = RequestMethod.PUT, consumes="application/json; charset=utf8")
    public ResponseEntity changeAge (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String age,
            @PathVariable String memberId
    ) throws IOException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        MemberVo memberVo = gson.fromJson(age, MemberVo.class);

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(age)) {
                httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                if(ObjectUtils.isEmpty(memberVo.getAge())) {
                    httpResponseVo.setHttpResponse("The parameter 'age' must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                    MemberVo member = this.memberRepository.findById(memberId);
                    member.setAge(memberVo.getAge());
                    this.memberRepository.save(member);
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(httpStatus).body(httpResponseVo);
    }

    @RequestMapping(value = "/getMemberList/{page}", method = RequestMethod.GET)
    public ResponseEntity getMemberList (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable int page
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        boolean isError = false;
        Page<MemberVo> memberList = null;

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(page)) {
                httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {
                if(ObjectUtils.isEmpty(page)) {
                    httpResponseVo.setHttpResponse("The parameter 'page' must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else {

                    PageRequest pr = new PageRequest(page, 20);

                    memberList = this.memberRepository.findByIdNotOrderByLastSignInDesc("", pr);
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;
                }
            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            isError = true;
        }

        if(isError) {
            return ResponseEntity.status(httpStatus).body(httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(memberList.getContent()));
        }
    }

    @RequestMapping(value = {"/getMemberInfo/{memberId}/", "/getMemberInfo/{memberId}"}, method = RequestMethod.GET)
    public ResponseEntity getMemberInfo (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("memberId")String memberId
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        MemberVo memberVo = new MemberVo();
        boolean isError = false;

        if(Auth.checkApiKey(apiKey)) {

            if(ObjectUtils.isEmpty(memberId)) {
                httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {
                if(ObjectUtils.isEmpty(memberId)) {
                    httpResponseVo.setHttpResponse("The 'memberId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else {
                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;

                    memberVo = this.memberRepository.findById(memberId);
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(memberVo));
        }
    }

    @RequestMapping(value = {"/getNearMemberList/{memberId}", "/getNearMemberList/{memberId}/"}, method = RequestMethod.GET)
    public ResponseEntity getNearMemberList (
            HttpServletRequest request,
            @PathVariable(value = "memberId") String memberId,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distanceMetres") int distanceMetres,
            @RequestParam(value = "page") int page,
            @RequestHeader(value = "apiKey", required = false)String apiKey
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        List<MemberVo> memberVoList = new ArrayList<>();
        boolean isError = false;

        if(Auth.checkApiKey(apiKey)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            if (ObjectUtils.isEmpty(memberId)) {
                httpResponseVo.setHttpResponse("The 'memberId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else if (ObjectUtils.isEmpty(latitude)) {
                httpResponseVo.setHttpResponse("The 'latitude' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else if (ObjectUtils.isEmpty(longitude)) {
                httpResponseVo.setHttpResponse("The 'longitude' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else if (ObjectUtils.isEmpty(distanceMetres)) {
                httpResponseVo.setHttpResponse("The 'distanceMetres' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else if (ObjectUtils.isEmpty(page)) {
                httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {

                httpStatus = HttpStatus.OK;
                httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());

                PageRequest pr = new PageRequest(page, 20);

                if(distanceMetres < 500) {
                    distanceMetres *= (1.414 * 1000);

                    WGS84Point startPoint = new WGS84Point(latitude, longitude);

                    WGS84Point nw = VincentyGeodesy.moveInDirection(startPoint, 300, distanceMetres);

                    WGS84Point se = VincentyGeodesy.moveInDirection(startPoint, 120, distanceMetres);

                    memberVoList = this.memberRepository.findByLocationLatBetweenAndLocationLonBetweenAndIdNot(se.getLatitude(), nw.getLatitude(), nw.getLongitude(), se.getLongitude(),memberId, pr).getContent();
                } else {
                    memberVoList = this.memberRepository.findByIdNotOrderByLastSignInDesc(memberId, pr).getContent();
//                    memberVoList = this.memberMapper.findMemberList();
                }

            }
        } else {
            httpStatus = HttpStatus.UNAUTHORIZED;
            httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(httpStatus).body(httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(memberVoList));
        }
    }
}
