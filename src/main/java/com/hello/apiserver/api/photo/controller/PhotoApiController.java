package com.hello.apiserver.api.photo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.photo.service.PhotoRepository;
import com.hello.apiserver.api.photo.vo.PhotoVo;
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
@RequestMapping(value = {"/photo", "/photo/"})
public class PhotoApiController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    MemberRepository memberRepository;

    private HttpResponseVo httpResponseVo = new HttpResponseVo();
    private HttpStatus httpStatus;

    @RequestMapping(value = {"/uploadPhoto", "/uploadPhoto/"}, method = RequestMethod.POST)
    public ResponseEntity uploadPhoto (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody String photoInfo
    ) throws IOException {

        boolean isError = false;
        PhotoVo photoVo = new PhotoVo();
        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(photoInfo)) {
                isError = true;
            } else {
                if(ObjectUtils.isEmpty(photoInfo)) {
                    isError = true;
                    this.httpResponseVo.setHttpResponse("The 'photoInfo' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    this.httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    isError = false;

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    photoVo = gson.fromJson(photoInfo, PhotoVo.class);
                    photoVo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));

                    this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    this.httpStatus = HttpStatus.OK;

                    photoVo = this.photoRepository.save(photoVo);
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
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(photoVo));
        }
    }

    @RequestMapping(value = {"/findPhotoVoByMemberId/{memberId}/{page}", "/findPhotoVoByMemberId/{memberId}/{page}/"}, method = RequestMethod.GET)
    public ResponseEntity findPhotoVoByMemberId (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable("memberId")String memberId,
            @PathVariable int page
    ) throws IOException {

        boolean isError = false;
        List<PhotoVo> photoVoList = new ArrayList<>();
        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            if(ObjectUtils.isEmpty(memberId)) {
                this.httpResponseVo.setHttpResponse("The 'photoInfo' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } if(ObjectUtils.isEmpty(page)) {
                this.httpResponseVo.setHttpResponse("The 'page' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {
                isError = false;
                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;

                PageRequest pr = new PageRequest(page, 15);
                photoVoList = this.photoRepository.findPhotoVoByMemberIdAndUseYnOrderByRegDtDesc(memberId, "Y", pr).getContent();
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
            return ResponseEntity.status(this.httpStatus).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(photoVoList));
        }
    }

    @RequestMapping(value = {"/updatePhoto", "/updatePhoto/"}, method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity changeProfilePhoto (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody String profileFileInfo
    ) throws IOException {

        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            PhotoVo photo = new Gson().fromJson(profileFileInfo, PhotoVo.class);
            if(photo.getUseYn().equals("Y")) {
                if(ObjectUtils.isEmpty(photo)) {
                    this.httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    this.httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    if(ObjectUtils.isEmpty(photo.getFileName())) {
                        this.httpResponseVo.setHttpResponse("The 'fileName' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                        this.httpStatus = HttpStatus.BAD_REQUEST;
                    } else if(ObjectUtils.isEmpty(photo.getOriginalImg())) {
                        this.httpResponseVo.setHttpResponse("The 'originalImg' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                        this.httpStatus = HttpStatus.BAD_REQUEST;
                    } else if(ObjectUtils.isEmpty(photo.getThumbnailImg())) {
                        this.httpResponseVo.setHttpResponse("The 'thumbnailImg' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                        this.httpStatus = HttpStatus.BAD_REQUEST;
                    } else {
                        PhotoVo photoVo;
                        if(!ObjectUtils.isEmpty(photo.getId())) {
                            photoVo = this.photoRepository.findByIdAndUseYn(photo.getId(), "Y");
                            photoVo.setFileName(photo.getFileName());
                            photoVo.setOriginalImg(photo.getOriginalImg());
                            photoVo.setThumbnailImg(photo.getThumbnailImg());
                        } else {
                            photo.setRegDt(new Date(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis()));
                            photoVo = photo;
                        }

                        this.photoRepository.save(photoVo);
                        this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                        this.httpStatus = HttpStatus.OK;
                    }
                }
            } else {
                PhotoVo photoVo = photoRepository.findByIdAndUseYn(photo.getId(), "Y");
                photo.setUseYn("N");
                this.photoRepository.save(photoVo);

                this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                this.httpStatus = HttpStatus.OK;
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = {"/updateProfilePhoto", "/updateProfilePhoto/"}, method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity updateProfilePhoto (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @RequestBody String profileFileInfo
    ) throws IOException {

        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            MemberVo memberVo = new Gson().fromJson(profileFileInfo, MemberVo.class);
            if(ObjectUtils.isEmpty(memberVo)) {
                this.httpResponseVo.setHttpResponse("The request body must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                this.httpStatus = HttpStatus.BAD_REQUEST;
            } else {

                if(ObjectUtils.isEmpty(memberVo.getId())) {
                    this.httpResponseVo.setHttpResponse("The 'memberId' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    this.httpStatus = HttpStatus.BAD_REQUEST;
                } else if(ObjectUtils.isEmpty(memberVo.getProfileUrl())) {
                    this.httpResponseVo.setHttpResponse("The 'profileUrl' parameter must not be null or empty", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
                    this.httpStatus = HttpStatus.BAD_REQUEST;
                } else {
                    MemberVo newMemberVo = memberRepository.findById(memberVo.getId());
                    newMemberVo.setProfileFile(memberVo.getProfileFile());
                    newMemberVo.setProfileUrl(memberVo.getProfileUrl());
                    newMemberVo.setProfileUrlOrg(memberVo.getProfileUrlOrg());
                    this.memberRepository.save(newMemberVo);

                    this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    this.httpStatus = HttpStatus.OK;
                }
            }
        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }

    @RequestMapping(value = {"/deletePhoto/{id}", "/deletePhoto/{id}/"}, method = RequestMethod.DELETE)
    @Transactional
    public ResponseEntity deletePhoto (
            HttpServletRequest request,
            @RequestHeader(value = "apiKey", required = false)String apiKey,
            @PathVariable String id
    ) throws IOException {

        this.httpResponseVo.setPath(request.getRequestURI());

        if(Auth.checkApiKey(apiKey)) {
            PhotoVo photoVo = this.photoRepository.findByIdAndUseYn(id, "Y");
            if(photoVo != null) {
                this.photoRepository.delete(photoVo);
            }

            this.httpResponseVo.setHttpResponse("", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
            this.httpStatus = HttpStatus.OK;

        } else {
            this.httpStatus = HttpStatus.UNAUTHORIZED;
            this.httpResponseVo.setHttpResponse("This api key is wrong! please check your api key!", HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        return ResponseEntity.status(this.httpStatus).body(this.httpResponseVo);
    }
}
