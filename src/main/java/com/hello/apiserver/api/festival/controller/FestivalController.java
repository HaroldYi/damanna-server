package com.hello.apiserver.api.festival.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.festival.service.FestivalRepository;
import com.hello.apiserver.api.festival.vo.FestivalVo;
import com.hello.apiserver.api.util.Auth.Auth;
import com.hello.apiserver.api.util.commonVo.HttpResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/festival")
public class FestivalController {

    @Autowired
    private FestivalRepository festivalRepository;

    @RequestMapping(value = {"/getFestivalList/{page}", "/getFestivalList/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity getSayList (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("page")int page
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

                festivalList = this.festivalRepository.findAll(pr).getContent();
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
}
