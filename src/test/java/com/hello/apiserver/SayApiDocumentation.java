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
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloserverApplication.class)
@WebAppConfiguration
public class SayApiDocumentation {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//                .alwaysDo(document("{class-name}/{method-name}/"))
//                .alwaysDo(this.document)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)).build();
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
    public void newSay() throws Exception {

//        Map<String, String> map = new HashMap<>();
//        map.put("memberId", "bqIDXpsVzlTL5X2cPMFROPHjtZn2");
//        map.put("message", "test message");
//
//        this.mockMvc.perform(post("/say/newSay").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(new Gson().toJson(map)))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("newSay", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void getSay() throws Exception {
//        this.mockMvc.perform(get("/say/getSay/{sayId}", "1").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("getSay", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void getSayList() throws Exception {
//        this.mockMvc.perform(get("/say/getSayList/{page}", "0").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("getSayList", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void getSayListByUid() throws Exception {
//        this.mockMvc.perform(get("/say/getSayListByUid/{memberId}/{page}", "r3gYviSRclWSfjvHCvRHd2gqdkj1", "0").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("getSayListByUid", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void newComment() throws Exception {

//        Map<String, String> map = new HashMap<>();
//        map.put("comment", "test message");
//        map.put("memberId", "bqIDXpsVzlTL5X2cPMFROPHjtZn2");
//        map.put("sayId", "4");
//
//        this.mockMvc.perform(post("/say/newComment").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(new Gson().toJson(map)))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("newComment", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void newCommentReply() throws Exception {

//        Map<String, String> map = new HashMap<>();
//        map.put("comment", "test message");
//        map.put("memberId", "bqIDXpsVzlTL5X2cPMFROPHjtZn2");
//        map.put("commentId", "4");
//
//        this.mockMvc.perform(post("/say/newCommentReply").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE).content(new Gson().toJson(map)))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("newCommentReply", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void likeSay() throws Exception {
//        this.mockMvc.perform(put("/say/likeSay/{sayId}/{memberId}", "18", "r3gYviSRclWSfjvHCvRHd2gqdkj1").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("likeSay", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }

    @Test
    public void deleteSay() throws Exception {
//        this.mockMvc.perform(delete("/say/deleteSay/{sayId}", "19").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andDo(MockMvcResultHandlers.print())
//                .andDo(document("deleteSay", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
//                .andExpect(status().isOk());
    }
}