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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HelloserverApplication.class)
@WebAppConfiguration
public class MemberApiDocumentation {

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
    public void newMember() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/member/newMember/{page}", "{page}").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("", is()))
//                .andDo(print())
//                .andDo(document("/users", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());
//                .andDo(document("newMember"));
//                .andDo(document("newMember", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));

//        .andDo(document("index-example",
//                        links(
//                                linkWithRel("notes").description("The <<resources-notes,Notes resource>>"),
//                                linkWithRel("tags").description("The <<resources-tags,Tags resource>>"),
//                                linkWithRel("profile").description("The ALPS profile for the service")),
//                        responseFields(
//                                fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

    @Test
    public void changeNickName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/member/changeNickName/{memberId}", "{memberId}").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("changeAge", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
//                .andDo(document("changeNickName"));

    }

    @Test
    public void changeAge() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/member/changeAge/{memberId}", "{memberId}").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("changeAge", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
//                .andDo(document("changeAge"));

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

        this.mockMvc.perform(MockMvcRequestBuilders.get("/member/getMemberList/{page}", 0).header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("getMemberList", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());
//                .andDo(document("getMemberList"));

    }

    @Test
    public void getMemberInfo() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/member/getMemberInfo/{memberId}", "{memberId}").header("apiToken", "{apiToken}").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("getMemberInfo", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), Preprocessors.preprocessResponse(Preprocessors.prettyPrint())))
                .andExpect(status().isOk());

    }
}