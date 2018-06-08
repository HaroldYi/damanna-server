package com.hello.apiserver.api.util.AppData;

import com.hello.apiserver.api.util.Auth.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping(value = "/appData")
public class AppDataController {

    @Autowired
    AppDataRepository appDataRepository;

    @RequestMapping(value = {"/getRegex", "/getRegex/"}, method = RequestMethod.GET)
    public List<RegexVo> getRegex(
            HttpServletResponse response,
            @RequestHeader(value = "apiKey", required = false)String apiKey
    ) throws IOException {

        if(Auth.checkApiKey(apiKey)) {
            Iterator<RegexVo> iterator = appDataRepository.findAll().iterator();
            List<RegexVo> regexList = new ArrayList<>();

            while (iterator.hasNext()) {
                regexList.add(iterator.next());
            }

            return regexList;
        } else  {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "This api key is wrong! please check your api key!");
        }

        return null;
    }
}
