package com.hello.apiserver.api.photo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.member.service.MemberRepository;
import com.hello.apiserver.api.member.vo.MemberVo;
import com.hello.apiserver.api.photo.service.PhotoRepository;
import com.hello.apiserver.api.photo.vo.PhotoVo;
import com.hello.apiserver.api.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping(value = {"/photo", "/photo/"})
public class PhotoApiController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    MemberRepository memberRepository;

    @RequestMapping(value = {"/uploadPhoto", "/uploadPhoto/"}, method = RequestMethod.POST)
    public String uploadPhoto (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody String photoInfo
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(photoInfo)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(photoInfo)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'photoInfo' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    PhotoVo photoVo = gson.fromJson(photoInfo, PhotoVo.class);
                    photoVo.setRegDt(new Date());

                    return gson.toJson(photoRepository.save(photoVo));
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/findPhotoVoByMemberId/{memberId}/{page}", "/findPhotoVoByMemberId/{memberId}/{page}/"}, method = RequestMethod.GET)
    public String findPhotoVoByMemberId (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable("memberId")String memberId,
            @PathVariable int page
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            if(ObjectUtils.isEmpty(memberId)) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            } else {
                if(ObjectUtils.isEmpty(memberId)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' parameter must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());

                    PageRequest pr = new PageRequest(page, 15);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    return gson.toJson(photoRepository.findPhotoVoByMemberIdAndUseYnOrderByRegDtDesc(memberId, "Y", pr).getContent());
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return null;
    }

    @RequestMapping(value = {"/updatePhoto", "/updatePhoto/"}, method = RequestMethod.PUT)
    @Transactional
    public String changeProfilePhoto (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody String profileFileInfo
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            PhotoVo photo = new Gson().fromJson(profileFileInfo, PhotoVo.class);
            if(photo.getUseYn().equals("Y")) {
                if(ObjectUtils.isEmpty(photo)) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
                } else {
                    if(ObjectUtils.isEmpty(photo.getFileName())) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'fileName' of request body must not be null or empty");
                    } else if(ObjectUtils.isEmpty(photo.getOriginalImg())) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'originalImg' request body must not be null or empty");
                    } else if(ObjectUtils.isEmpty(photo.getThumbnailImg())) {
                        response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'thumbnailImg' request body must not be null or empty");
                    } else {
                        response.setStatus(HttpStatus.OK.value());

                        PhotoVo photoVo;
                        if(!ObjectUtils.isEmpty(photo.getId())) {
                            photoVo = photoRepository.findByIdAndUseYn(photo.getId(), "Y");
                            photoVo.setFileName(photo.getFileName());
                            photoVo.setOriginalImg(photo.getOriginalImg());
                            photoVo.setThumbnailImg(photo.getThumbnailImg());
                        } else {
                            photo.setRegDt(new Date());
                            photoVo = photo;
                        }

                        photoRepository.save(photoVo);
                        return HttpStatus.OK.toString();
                    }
                }
            } else {
                PhotoVo photoVo = photoRepository.findByIdAndUseYn(photo.getId(), "Y");
                photo.setUseYn("N");
                photoRepository.save(photoVo);
                return "OK";
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = {"/updateProfilePhoto", "/updateProfilePhoto/"}, method = RequestMethod.PUT)
    @Transactional
    public String updateProfilePhoto (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @RequestBody String profileFileInfo
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {

            MemberVo memberVo = new Gson().fromJson(profileFileInfo, MemberVo.class);
            if(ObjectUtils.isEmpty(memberVo)) {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "The request body must not be null or empty");
            } else {

                if(ObjectUtils.isEmpty(memberVo.getId())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'memberId' of request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getProfileFile())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'fileName' of request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getProfileUrlOrg())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'originalImg' request body must not be null or empty");
                } else if(ObjectUtils.isEmpty(memberVo.getProfileUrl())) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "The 'thumbnailImg' request body must not be null or empty");
                } else {
                    response.setStatus(HttpStatus.OK.value());
                    MemberVo newMemberVo = memberRepository.findById(memberVo.getId());
                    newMemberVo.setProfileFile(memberVo.getProfileFile());
                    newMemberVo.setProfileUrl(memberVo.getProfileUrl());
                    newMemberVo.setProfileUrlOrg(memberVo.getProfileUrlOrg());
                    memberRepository.save(newMemberVo);
                    return HttpStatus.OK.toString();
                }
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }

    @RequestMapping(value = {"/deletePhoto/{id}", "/deletePhoto/{id}/"}, method = RequestMethod.DELETE)
    @Transactional
    public String deletePhoto (
            HttpServletResponse response,
            @RequestHeader(value = "apiToken")String apiToken,
            @PathVariable String id
    ) throws IOException {

        if(Auth.checkToken(apiToken)) {
            PhotoVo photoVo = this.photoRepository.findByIdAndUseYn(id, "Y");
            if(photoVo != null) {
                this.photoRepository.delete(photoVo);
            }

            return HttpStatus.OK.toString();
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This token is wrong! please check your token!");
        }

        return "";
    }
}
