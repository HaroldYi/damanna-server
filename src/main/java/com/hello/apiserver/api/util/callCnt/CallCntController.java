package com.hello.apiserver.api.util.callCnt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/callCnt", "/callCnt/"})
public class CallCntController {

    @Autowired
    private CallCntRepository callCntRepository;

    @RequestMapping(value = {"", "/"})
    public int checkAppVersion() {

        CallCntVo callCntVo = callCntRepository.findAll().iterator().next();
        callCntVo.setCnt(callCntVo.getCnt() + 1);

        return callCntRepository.save(callCntVo).getCnt();
    }
}
