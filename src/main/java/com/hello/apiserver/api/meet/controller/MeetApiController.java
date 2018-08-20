package com.hello.apiserver.api.meet.controller;

import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.util.VincentyGeodesy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hello.apiserver.api.like.service.LikeRepository;
import com.hello.apiserver.api.meet.mapper.MeetMapper;
import com.hello.apiserver.api.meet.service.MeetRepository;
import com.hello.apiserver.api.meet.service.NewMeetRepository;
import com.hello.apiserver.api.meet.vo.MeetVo;
import com.hello.apiserver.api.meet.vo.NearMeetVo;
import com.hello.apiserver.api.meet.vo.NewMeetVo;
import com.hello.apiserver.api.member.service.MeetBannedMemberRepository;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MeetBannedMemberVo;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.comment.service.CommentReplyRepository;
import com.hello.apiserver.api.comment.service.CommentRepository;
import com.hello.apiserver.api.say.service.LikeSayRepository;
import com.hello.apiserver.api.like.vo.LikeSayVo;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.vo.HttpResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@RestController
@RequestMapping(value = {"/meet", "/meet/"})
public class MeetApiController {

    @Autowired
    private MeetMapper meetMapper;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private NewMeetRepository newMeetRepository;

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

    private HttpResponseVo httpResponseVo = new HttpResponseVo();
    private HttpStatus httpStatus;

    @RequestMapping(value = "/newMeet", method = RequestMethod.POST)
    public ResponseEntity newMeet (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        this.httpResponseVo.setPath(request.getRequestURI());
        NewMeetVo meetVo = new NewMeetVo();
        boolean isError = false;

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The 'msg' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                isError = true;
            } else {
//                response.setStatus(HttpStatus.OK.value());

                isError = false;

                meetVo = new Gson().fromJson(body, NewMeetVo.class);
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

                if(!ObjectUtils.isEmpty(meetVo.getPassword())) {
                    meetVo.setHasPassword(true);
                }

                meetVo = this.newMeetRepository.save(meetVo);
                this.httpStatus = HttpStatus.OK;
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(meetVo));
        }
    }

    @RequestMapping(value = {"/getMeet/{meetId}", "/getMeet/{meetId}/"}, method = RequestMethod.GET)
    public ResponseEntity getSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("meetId")String meetId
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        MeetVo meetVo = new MeetVo();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(meetId)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The 'meetId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                isError = true;
            } else {
                response.setStatus(HttpStatus.OK.value());

                meetVo = this.meetRepository.findByIdAndUseYn(meetId, "Y");
                if(meetVo.getMember() == null) {
                    MemberVo memberVo = new MemberVo();
                    memberVo.setName(meetVo.getTitle());
                    memberVo.setProfileUrl(meetVo.getThumbnailImg());
                    memberVo.setProfileUrlOrg(meetVo.getOriginalImg());

                    meetVo.setMember(memberVo);
                    meetVo.setTitle("");
                }

                List<LikeSayVo> likeSayVoList = this.likeRepository.findByMeetIdAndSortation(meetId, "M");
                meetVo.setLikeSay(likeSayVoList);

                this.httpStatus = HttpStatus.OK;
                isError = false;
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(meetVo));
        }
    }

    @RequestMapping(value = {"/getMeetList/{page}", "/getMeetList/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity getSayList (
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

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = {"/getNearMeetList", "/getNearMeetList/"}, method = RequestMethod.GET)
    public ResponseEntity getNearMeetList (
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

        boolean isError = false;
        List<NearMeetVo> meetVoList = new ArrayList<>();

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

            this.httpStatus = HttpStatus.OK;
            isError = false;
//            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(meetVoList));
        }
    }

    @RequestMapping(value = {"/getMeetListByUid/{memberId}/{page}", "/getMeetListByUid/{memberId}/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity getSayListByUid (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String memberId,
            @PathVariable int page
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        boolean isError = false;
        List<NearMeetVo> meetVoList = new ArrayList<>();

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
//                response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'msg' parameter must not be null or empty");
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            } else {

                this.httpStatus = HttpStatus.OK;

                Map<String, Object> map = new HashMap<>();
                map.put("memberId", memberId);
                map.put("page", page);

                meetVoList = this.meetMapper.getMeetListByUid(map);
                for(NearMeetVo meetVo : meetVoList) {

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
            }
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(meetVoList));
        }
    }

    @RequestMapping(value = "/deleteMeet/{meetId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSay (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String meetId
    ) throws IOException {
        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(meetId)) {
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The 'sayId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            } else {
                this.httpStatus = HttpStatus.OK;

                MeetVo meetVo = this.meetRepository.findByIdAndUseYn(meetId, "Y");
                if(meetVo != null) {
                    this.meetRepository.delete(meetVo);
                }
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = "/updateChannelUrl", method = RequestMethod.PUT)
    public ResponseEntity updateChannelUrl (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            } else {
                this.httpStatus = HttpStatus.OK;

                MeetVo meetVo = new Gson().fromJson(body, MeetVo.class);
                String channelUrl = meetVo.getChannelUrl();

                meetVo = this.meetRepository.findByIdAndUseYn(meetVo.getId(), "Y");
                meetVo.setChannelUrl(channelUrl);

                this.meetRepository.save(meetVo);
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    public ResponseEntity checkPassword (
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                this.httpStatus = HttpStatus.BAD_REQUEST;
                this.httpResponseVo.setHttpResponse("The parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            } else {
                this.httpStatus = HttpStatus.OK;

                Type mapType = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, Object> map = new Gson().fromJson(body, mapType);
                map.put("channelUrl", map.get("channelUrl"));
                map.put("password", map.get("password"));

                int cnt = this.meetMapper.checkPassword(map);
                if(cnt == 0) {
                    this.httpStatus = HttpStatus.UNAUTHORIZED;
                    this.httpResponseVo.setHttpResponse("Password is wrong! please check password", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                } else {
                    this.httpStatus = HttpStatus.OK;
                }
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }
}
