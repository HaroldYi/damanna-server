package com.hello.apiserver.api.coupon.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hello.apiserver.api.coupon.service.CouponRepository;
import com.hello.apiserver.api.coupon.vo.CouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/coupon")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @RequestMapping(value = "/getCouponUrl", method = RequestMethod.GET)
    public ResponseEntity getCouponUrl() {
        CouponVo couponVo = couponRepository.findById("1");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON_UTF8).body(gson.toJson(couponVo));
    }
}
