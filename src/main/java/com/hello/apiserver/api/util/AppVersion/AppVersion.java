package com.hello.apiserver.api.util.AppVersion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/app", "/appVersion"})
public class AppVersion {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public float checkAndroidAppVersion() {
        return appVersionRepository.findById("0").getAndroid();
    }

    @RequestMapping(value = {"/version", "/version/"}, method = RequestMethod.GET)
    public AppVersionVo checkAppVersion() {
        return appVersionRepository.findById("0");
    }
}
