package com.hello.apiserver.api.coupon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/coupon")
public class CouponControllerTemp {

    @GetMapping(value = "")
    public @ResponseBody
    String getCouponUrl() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <script src=\"http://vpass.co.kr/coupon/js/iframeControl.js\" type=\"text/javascript\"></script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<iframe width=\"100%\" frameborder=\"0\" id=\"cpframe\" name=\"cpframe\" src=\"http://hellostudio.vpass.co.kr/coupon\" height=\"34987\"></iframe>\n" +
                "\n" +
                "<canvas id=\"__gesture_canvas__\" width=\"0\" height=\"0\" style=\"z-index: 100000000; position: fixed; background: transparent !important; top: 0px; left: 0px;\"></canvas>\n" +
                "<span id=\"__gesture_preview__\" style=\"z-index: 100000001; position: absolute; background: rgba(255, 255, 225, 0.85); margin: 0px; border: 1px solid black; white-space: nowrap; display: none;\"></span>\n" +
                "</body>\n" +
                "</html>";
    }
}