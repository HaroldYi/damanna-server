/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hello.apiserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloserverApplication.class)
@WebAppConfiguration
public class MemberApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    public RestDocumentationResultHandler document;

//    @Autowired
//    private NoteRepository noteRepository;
//
//    @Autowired
//    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

//        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}/", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//                .alwaysDo(document("{class-name}/{method-name}/"))
//                .alwaysDo(this.document)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation).uris().withHost("Helloapi.ap-northeast-2.elasticbeanstalk.com")).build();
    }

//    @Test
//    public void errorExample() throws Exception {
//        this.mockMvc
//                .perform(get("/error")
//                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
//                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
//                                "/notes")
//                        .requestAttr(RequestDispatcher.ERROR_MESSAGE,
//                                "The tag 'http://localhost:8080/tags/123' does not exist"))
//                .andDo(print()).andExpect(status().isBadRequest())
//                .andExpect(jsonPath("error", is("Bad Request")))
//                .andExpect(jsonPath("timestamp", is(notNullValue())))
//                .andExpect(jsonPath("status", is(400)))
//                .andExpect(jsonPath("path", is(notNullValue())))
//                .andDo(document("error-example",
//                        responseFields(
//                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
//                                fieldWithPath("message").description("A description of the cause of the error"),
//                                fieldWithPath("path").description("The path to which the request was made"),
//                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
//                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
//    }

    @Test
    public void newMember() throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("id", "test ID"+System.currentTimeMillis());
        map.put("name", "test name");
        map.put("email", "test email" + System.currentTimeMillis());
        map.put("age", "22");
        map.put("profileUrl", "https://");
        map.put("profileUrlOrg", "https://");
        map.put("profileFile", "https://");
        map.put("clientToken", "test token");
        map.put("genderCode", "M");
        map.put("locationLat", "10");
        map.put("locationLon", "10");
        map.put("point", "10");

        this.mockMvc.perform(MockMvcRequestBuilders.post("/member/newMember").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(new Gson().toJson(map)))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("newMember", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
    }

    @Test
    public void changeNickName() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apiToken", "{apiToken}");
        httpHeaders.add("Content-type", "application/json; charset=utf8");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/member/changeNickName/{memberId}", "r3gYviSRclWSfjvHCvRHd2gqdkj1").headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content("{\"name\" : \"Harolddd\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("changeNickName", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
    }

    @Test
    public void changeAge() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apiToken", "{apiToken}");
        httpHeaders.add("Content-type", "application/json; charset=utf8");

        this.mockMvc.perform(MockMvcRequestBuilders.put("/member/changeAge/{memberId}", "r3gYviSRclWSfjvHCvRHd2gqdkj1").headers(httpHeaders).accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content("{\"age\" : \"29\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("changeAge", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
    }

    @Test
    public void getMemberList() throws Exception {

//        this.document.snippets(
//                HypermediaDocumentation.links(
//                        HypermediaDocumentation.linkWithRel("self").description("This <<resources-note,note>>"),
//                        HypermediaDocumentation.linkWithRel("note-tags").description("This note's <<resources-note-tags,tags>>")),
//                PayloadDocumentation.responseFields(
//                        PayloadDocumentation.fieldWithPath("title").description("The title of the note"),
//                        PayloadDocumentation.fieldWithPath("body").description("The body of the note"),
//                        PayloadDocumentation.fieldWithPath("_links").description("<<resources-note-links,Links>> to other resources")));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/member/getMemberList/{page}", 0).header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("getMemberList", RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("page").description("The page of List"))))
                .andExpect(status().isOk());
//                .andDo(document("getMemberList"));

    }

    @Test
    public void getMemberInfo() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/member/getMemberInfo/{memberId}", "r3gYviSRclWSfjvHCvRHd2gqdkj1").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("getMemberInfo", RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("memberId").description("The identifier of Member"))))
                .andExpect(status().isOk());

    }
}