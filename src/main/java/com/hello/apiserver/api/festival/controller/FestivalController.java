package com.hello.apiserver.api.festival.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.festival.service.FestivalMeetRepository;
import com.hello.apiserver.api.festival.service.FestivalRepository;
import com.hello.apiserver.api.festival.vo.FestivalMeetVo;
import com.hello.apiserver.api.festival.vo.FestivalVo;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/festival")
public class FestivalController {

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FestivalMeetRepository festivalMeetRepository;

    @RequestMapping(value = {"/getFestivalList", "/getFestivalList/"}, method = RequestMethod.GET)
    public ResponseEntity getSayList (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestParam(value = "meetStartDt", required = false) String meetStartDt,
            @RequestParam(value = "meetEndDt", required = false) String meetEndDt,
            @RequestParam(value = "page") int page
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        boolean isError = false;
        List<FestivalVo> festivalList = new ArrayList<>();

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(page)) {
                httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {
                httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                httpStatus = HttpStatus.OK;

                PageRequest pr = new PageRequest(page, 20);

                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMdd");

                LocalDateTime startDatetime = null;
                LocalDateTime endDatetime = null;

                Date meetStartDate;
                Date meetEndDate;

                if(!ObjectUtils.isEmpty(meetStartDt) && !ObjectUtils.isEmpty(meetEndDt)) {
                    startDatetime = LocalDateTime.of(LocalDate.parse(meetStartDt, sdf), LocalTime.of(0,0,0)); //어제 00:00:00
                    endDatetime = LocalDateTime.of(LocalDate.parse(meetEndDt, sdf), LocalTime.of(23,59,59)); // 23:59:59

                    meetStartDate = Date.from(startDatetime.toInstant(ZoneOffset.of("+9")));
                    meetEndDate = Date.from(endDatetime.toInstant(ZoneOffset.of("+9")));

                    festivalList = this.festivalRepository.findByEventstartdateAfterAndEventenddateBeforeOrderByEventstartdateAsc(meetStartDate, meetEndDate, pr).getContent();
                } else  {
                    startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0)); //어제 00:00:00
                    meetStartDate = Date.from(startDatetime.toInstant(ZoneOffset.of("+9")));

                    festivalList = this.festivalRepository.findByEventstartdateAfterOrderByEventstartdateAsc(meetStartDate, pr).getContent();
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
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(festivalList));
        }
    }

    @RequestMapping(value = {"/getFestival/{festivalId}", "/getFestival/{festivalId}/"}, method = RequestMethod.GET)
    public ResponseEntity getFestival (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("festivalId")String festivalId
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        boolean isError = false;
        FestivalVo festivalVo = new FestivalVo();

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(festivalId)) {
                httpResponseVo.setHttpResponse("The 'festivalId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {
                httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                httpStatus = HttpStatus.OK;
                festivalVo = this.festivalRepository.findByContentid(festivalId);
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
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(festivalVo));
        }
    }

    @RequestMapping(value = {"/updateChannelUrl", "/updateChannelUrl/"}, method = RequestMethod.PUT)
    public ResponseEntity updateChannelUrl (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody(required = false)String body
    ) throws IOException {

        HttpResponseVo httpResponseVo = new HttpResponseVo();
        httpResponseVo.setResponse("httpreponse");
        httpResponseVo.setTimestamp(new Date().getTime());
        httpResponseVo.setPath(request.getRequestURI());

        HttpStatus httpStatus;
        boolean isError = false;
        FestivalVo festivalVo = new FestivalVo();

//        apiKey = new Gson().fromJson(apiKey, String.class);

        if(Auth.checkApiKey(apiKey)) {
            if (ObjectUtils.isEmpty(body)) {
                httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                httpStatus = HttpStatus.BAD_REQUEST;
                isError = true;
            } else {

                FestivalMeetVo festivalMeetVo = new Gson().fromJson(body, FestivalMeetVo.class);
                if(ObjectUtils.isEmpty(festivalMeetVo.getFestivalId())) {
                    httpResponseVo.setHttpResponse("The 'festivalId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else if(ObjectUtils.isEmpty(festivalMeetVo.getChannelUrl())) {
                    httpResponseVo.setHttpResponse("The 'channelUrl' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else if(ObjectUtils.isEmpty(festivalMeetVo.getMeetDt())) {
                    httpResponseVo.setHttpResponse("The 'meetDt' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    httpStatus = HttpStatus.BAD_REQUEST;
                    isError = true;
                } else {

                    httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    httpStatus = HttpStatus.OK;

                    this.festivalMeetRepository.save(festivalMeetVo);
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
            return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(festivalVo));
        }
    }
}
