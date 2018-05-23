package com.hello.apiserver.api.say.controller;

import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.say.mapper.SayMapper;
import com.hello.apiserver.api.say.service.SayRepository;
import com.hello.apiserver.api.say.vo.NearSayVo;
import com.hello.apiserver.api.say.vo.SayVo;
import com.hello.apiserver.api.util.Auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = {"/say", "/say/"})
public class SayApiController {

    @Autowired
    private SayMapper sayMapper;

    @Autowired
    private SayRepository sayRepository;

    @Autowired
    private LikeRepository likeRepository;

    @RequestMapping(value = "/newSay", method = RequestMethod.POST)
    public String newSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                SayVo sayVo = new Gson().fromJson(body, SayVo.class);
                sayVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                sayVo.setUseYn("Y");

                this.sayRepository.save(sayVo);
                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }

    @RequestMapping(value = {"/getSay/{sayId}", "/getSay/{sayId}/"}, method = RequestMethod.GET)
    public String getSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("sayId")String sayId
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(sayId)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                return gson.toJson(this.sayRepository.findByIdAndUseYn(sayId, "Y"));
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getSayList/{page}", "/getSayList/{page}/"}, method = RequestMethod.GET)
    public String getSayList (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("page")int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                PageRequest pr = new PageRequest(page, 20);
                response.setStatus(HttpStatus.OK.value());

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                List<SayVo> sayVoList = this.sayRepository.findAllByUseYnOrderByRegDtDesc("Y", pr).getContent();
                return gson.toJson(sayVoList);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getNearSayList", "/getNearSayList/"}, method = RequestMethod.GET)
    public String getNearSayList (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distanceMetres") int distanceMetres,
            @RequestParam(value = "page") int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(latitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'latitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(longitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'longitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(distanceMetres)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'distanceMetres' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(page)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'page' parameter must not be null or empty");
            } else {

                page *= 20;

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                PageRequest pr = new PageRequest(page, 20);
                List<NearSayVo> sayVoList;
                Map<String, Object> map = new HashMap<>();

                if(distanceMetres < 500) {
                    distanceMetres *= (1.414 * 1000);

                    WGS84Point startPoint = new WGS84Point(latitude, longitude);

                    WGS84Point nw = VincentyGeodesy.moveInDirection(startPoint, 300, distanceMetres);

                    WGS84Point se = VincentyGeodesy.moveInDirection(startPoint, 120, distanceMetres);

                    map.put("seLat", se.getLatitude());
                    map.put("seLon", se.getLongitude());
                    map.put("nwLat", nw.getLatitude());
                    map.put("nwLon", nw.getLongitude());
                    map.put("page", page);

//                    sayVoList = this.sayRepository.findByLocationLatBetweenAndLocationLonBetween(se.getLatitude(), nw.getLatitude(), nw.getLongitude(), se.getLongitude(), pr).getContent();
                } else {
//                    map.put("seLat", se.getLatitude());
//                    map.put("seLon", se.getLongitude());
//                    map.put("nwLat", nw.getLatitude());
//                    map.put("nwLon", nw.getLongitude());
                    map.put("page", page);
                }

                sayVoList = this.sayMapper.findSayByDistance(map);

                int i = 0;
                for(NearSayVo sayVo : sayVoList) {

                    map.put("sayId", sayVo.getId());
                    map.put("sortation", "S");

                    List<LikeSayVo> likeSayVoList = this.likeRepository.findBySayIdAndSortation(sayVo.getId(), "S");

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(sayVo.getMemberId());
                    memberVo.setName(sayVo.getName());
                    memberVo.setClientToken(sayVo.getClientToken());
                    memberVo.setProfileUrl(sayVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(sayVo.getProfileUrlOrg());
                    memberVo.setProfileFile(sayVo.getProfileFile());

                    sayVo.setMember(memberVo);
                    sayVo.setLikeSay(likeSayVoList);
                    sayVoList.set(i++, sayVo);
                }

                return gson.toJson(sayVoList);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getSayListByUid/{memberId}/{page}", "/getSayListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
    public String getSayListByUid (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String memberId,
            @PathVariable int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

//                PageRequest pr = new PageRequest(page, 20);
//
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                return gson.toJson(this.sayRepository.findByMemberIdAndUseYnOrderByRegDtDesc(memberId, "Y", pr).getContent());

                Map<String, Object> map = new HashMap<>();
                map.put("memberId", memberId);
                map.put("page", page);

                List<NearSayVo> sayVoList = this.sayMapper.getSayListByUid(map);
                for(NearSayVo sayVo : sayVoList) {

                    map.put("sayId", sayVo.getId());

                    List<String> likeSayVoListStr = this.sayMapper.findLikeMemberList(map);
                    List<LikeSayVo> likeSayVoList = new ArrayList<>();
                    for(String like : likeSayVoListStr) {
                        LikeSayVo likeSayVo = new LikeSayVo();
                        MemberVo memberVo = new MemberVo();
                        memberVo.setId(like);

                        likeSayVo.setMember(memberVo);
                        likeSayVoList.add(likeSayVo);
                    }

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(sayVo.getMemberId());
                    memberVo.setName(sayVo.getName());
                    memberVo.setClientToken(sayVo.getClientToken());
                    memberVo.setProfileUrl(sayVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(sayVo.getProfileUrlOrg());
                    memberVo.setProfileFile(sayVo.getProfileFile());

                    sayVo.setMember(memberVo);
                    sayVo.setLikeSay(likeSayVoList);
                }

                return gson.toJson(sayVoList);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = "/deleteSay/{sayId}", method = RequestMethod.DELETE)
    public String deleteSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String sayId
    ) throws IOException {
        Gson gson = new Gson();

//        apiKey = gson.fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(sayId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                SayVo sayVo = this.sayRepository.findByIdAndUseYn(sayId, "Y");
                if(sayVo != null) {
//                    sayVo.setUseYn("N");
//                    this.sayRepository.save(sayVo);
                    this.sayRepository.delete(sayVo);
                }

                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }
}
