package com.hello.apiserver.api.place.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.photo.service.PhotoRepository;
import com.hello.apiserver.api.photo.vo.PhotoVo;
import com.hello.apiserver.api.place.mapper.PlaceRankMapper;
import com.hello.apiserver.api.place.vo.PlaceVo;
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
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = {"/place", "/place/"})
public class PlaceApiController {

    @Autowired
    PlaceRankMapper placeRankMapper;

    private HttpResponseVo httpResponseVo = new HttpResponseVo();
    private HttpStatus httpStatus;

    @RequestMapping(value = {"/getPlaceRank", "/getPlaceRank/"}, method = RequestMethod.GET)
    public ResponseEntity findPhotoVoByMemberId (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey
    ) {

        boolean isError = false;
        List<PlaceVo> placeVoList = new ArrayList<>();
        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            this.httpStatus = HttpStatus.OK;
            placeVoList = placeRankMapper.getPlaceRank();
        } else {
            isError = true;
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(isError) {
            return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
        } else {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(placeVoList));
        }
    }
}
