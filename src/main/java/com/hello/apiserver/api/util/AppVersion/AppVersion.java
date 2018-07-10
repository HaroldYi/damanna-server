package com.hello.apiserver.api.util.AppVersion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/appVersion", "/appVersion/"})
public class AppVersion {

    @Autowired
    private AppVersionRepository appVersionRepository;

    @RequestMapping(value = {"", "/android"})
    public float checkAndroidAppVersion() {
        return appVersionRepository.findById("0").getAndroid();
    }

    @RequestMapping(value = {"ios", "/ios"})
    public float checkIosAppVersion() {
        return appVersionRepository.findById("0").getIos();
    }
}
