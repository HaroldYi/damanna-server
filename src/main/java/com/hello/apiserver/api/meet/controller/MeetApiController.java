package com.hello.apiserver.api.meet.controller;

import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.meet.mapper.MeetMapper;
import com.hello.apiserver.api.meet.service.MeetRepository;
import com.hello.apiserver.api.meet.vo.MeetVo;
import com.hello.apiserver.api.meet.vo.NearMeetVo;
import com.hello.apiserver.api.member.service.MeetBannedMemberRepository;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MeetBannedMemberVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.comment.service.CommentReplyRepository;
import com.hello.apiserver.api.comment.service.CommentRepository;
import com.hello.apiserver.api.say.service.LikeSayRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
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
@RequestMapping(value = {"/meet", "/meet/"})
public class MeetApiController {

    @Autowired
    private MeetMapper meetMapper;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private LikeSayRepository likeSayRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MeetBannedMemberRepository meetBannedMemberRepository;

    @RequestMapping(value = "/newMeet", method = RequestMethod.POST)
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

                MeetVo meetVo = new Gson().fromJson(body, MeetVo.class);
                meetVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                meetVo.setUseYn("Y");

                Date meetStartDt = meetVo.getMeetStartDt();
                Date meetEndDt = meetVo.getMeetEndDt();

                // 시작날짜 0시 ~ 종료날짜 23시 59분 59초까지
                Calendar meetStartCalendar = Calendar.getInstance();
                meetStartCalendar.setTime(meetStartDt);
                meetStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
                meetStartCalendar.set(Calendar.MINUTE, 0);
                meetStartCalendar.set(Calendar.SECOND, 0);

                Calendar meetEndCalendar = Calendar.getInstance();
                meetEndCalendar.setTime(meetEndDt);
                meetEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
                meetEndCalendar.set(Calendar.MINUTE, 59);
                meetEndCalendar.set(Calendar.SECOND, 59);

                meetStartDt.setTime(meetStartCalendar.getTimeInMillis());
                meetEndDt.setTime(meetEndCalendar.getTimeInMillis());

                meetVo.setMeetStartDt(meetStartDt);
                meetVo.setMeetEndDt(meetEndDt);

                if(ObjectUtils.isEmpty(meetVo.getMemberLimit())) {
                    meetVo.setMemberLimit("4");
                }

                this.meetRepository.save(meetVo);
                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }

    @RequestMapping(value = {"/getMeet/{meetId}", "/getMeet/{meetId}/"}, method = RequestMethod.GET)
    public String getSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("meetId")String meetId
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(meetId)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                MeetVo meetVo = this.meetRepository.getMeet(meetId);
                List<LikeSayVo> likeSayVoList = this.likeRepository.findByMeetIdAndSortation(meetId, "M");

                meetVo.setLikeSay(likeSayVoList);
                return gson.toJson(meetVo);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getMeetList/{page}", "/getMeetList/{page}/"}, method = RequestMethod.GET)
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
//                PageRequest pr = new PageRequest(page, 20);
//                response.setStatus(HttpStatus.OK.value());
//
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                List<MeetVo> sayVoList = this.meetRepository.findAllByUseYnOrderByRegDtDesc("Y", pr).getContent();
//                return gson.toJson(sayVoList);

//                return new Gson().toJson(meetRepository.getMeetList(0));
//                meetMapper.findMeetByDistance();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getNearMeetList", "/getNearMeetList/"}, method = RequestMethod.GET)
    public String getNearMeetList (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude,
            @RequestParam(value = "distanceMetres") int distanceMetres,
            @RequestParam(value = "meetStartDt", required = false) String meetStartDt,
            @RequestParam(value = "meetEndDt", required = false) String meetEndDt,
            @RequestParam(value = "page") int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            /*
            if (ObjectUtils.isEmpty(latitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'latitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(longitude)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'longitude' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(distanceMetres)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'distanceMetres' parameter must not be null or empty");
            } else if (ObjectUtils.isEmpty(page)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'page' parameter must not be null or empty");
            } else {
            */
                page *= 20;

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                PageRequest pr = new PageRequest(page, 20);
                List<NearMeetVo> meetVoList;
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
                } else {
                    map.put("page", page);
                }

                if(!ObjectUtils.isEmpty(meetStartDt)) {
                    map.put("meetStartDt", meetStartDt);
                }

                if(!ObjectUtils.isEmpty(meetEndDt)) {
                    map.put("meetEndDt", meetEndDt);
                }

                meetVoList = this.meetMapper.findMeetByDistance(map);

                int i = 0;
//
                for(NearMeetVo meetVo : meetVoList) {

                    map.put("sayId", meetVo.getId());
                    map.put("sortation", "M");

                    List<LikeSayVo> likeSayVoList = this.likeRepository.findByMeetIdAndSortation(meetVo.getId(), "M");
                    List<MeetBannedMemberVo> meetBannedMemberList = this.meetBannedMemberRepository.findByChannelUrl(meetVo.getChannelUrl());
//                    List<String> likeSayVoListStr = this.meetMapper.findLikeMemberList(map);
//                    List<LikeSayVo> likeSayVoList = new ArrayList<>();
//                    for(String like : likeSayVoListStr) {
//                        LikeSayVo likeSayVo = new LikeSayVo();
//                        MemberVo memberVo = new MemberVo();
//                        memberVo.setId(like);
//
//                        likeSayVo.setMember(memberVo);
//                        likeSayVoList.add(likeSayVo);
//                    }

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(meetVo.getMemberId());
                    memberVo.setName(meetVo.getName());
                    memberVo.setClientToken(meetVo.getClientToken());
                    memberVo.setProfileUrl(meetVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(meetVo.getProfileUrlOrg());
                    memberVo.setProfileFile(meetVo.getProfileFile());

                    meetVo.setMember(memberVo);
                    meetVo.setLikeSay(likeSayVoList);
                    meetVo.setMeetBannedMemberList(meetBannedMemberList);
                    meetVoList.set(i++, meetVo);
                }

                return gson.toJson(meetVoList);
//            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = {"/getMeetListByUid/{memberId}/{page}", "/getMeetListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
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

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                Map<String, Object> map = new HashMap<>();
                map.put("memberId", memberId);
                map.put("page", page);

                List<NearMeetVo> sayVoList = this.meetMapper.getMeetListByUid(map);
                for(NearMeetVo meetVo : sayVoList) {

                    List<LikeSayVo> likeSayVoList = this.likeRepository.findByMeetIdAndSortation(meetVo.getId(), "M");
                    List<MeetBannedMemberVo> meetBannedMemberList = this.meetBannedMemberRepository.findByChannelUrl(meetVo.getChannelUrl());

                    MemberVo memberVo = new MemberVo();
                    memberVo.setId(meetVo.getMemberId());
                    memberVo.setName(meetVo.getName());
                    memberVo.setClientToken(meetVo.getClientToken());
                    memberVo.setProfileUrl(meetVo.getProfileUrl());
                    memberVo.setProfileUrlOrg(meetVo.getProfileUrlOrg());
                    memberVo.setProfileFile(meetVo.getProfileFile());
                    meetVo.setMeetBannedMemberList(meetBannedMemberList);

                    meetVo.setMember(memberVo);
                    meetVo.setLikeSay(likeSayVoList);
                }

                return gson.toJson(sayVoList);
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }

    @RequestMapping(value = "/deleteMeet/{meetId}", method = RequestMethod.DELETE)
    public String deleteSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String meetId
    ) throws IOException {
        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(meetId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'sayId' request body must not be null or empty");
            } else {
                response.setStatus(HttpStatus.OK.value());

                MeetVo meetVo = this.meetRepository.findByIdAndUseYn(meetId, "Y");
                if(meetVo != null) {
                    this.meetRepository.delete(meetVo);
                }

                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }

    @RequestMapping(value = "/updateChannelUrl", method = RequestMethod.PUT)
    public String updateChannelUrl (
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

                MeetVo meetVo = new Gson().fromJson(body, MeetVo.class);
                String channelUrl = meetVo.getChannelUrl();

                meetVo = this.meetRepository.findByIdAndUseYn(meetVo.getId(), "Y");
                meetVo.setChannelUrl(channelUrl);

                this.meetRepository.save(meetVo);
                return HttpStatus.OK.toString();
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return "";
    }
}
