package com.tools.seoultech.timoproject.controller;


import com.tools.seoultech.timoproject.global.config.TestSecurityConfig;
import com.tools.seoultech.timoproject.global.error.ViewExceptionHandler;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.riot.controller.BasicController;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("[View Controller]")
@ContextConfiguration(classes = {BasicController.class, ViewExceptionHandler.class})
@WebMvcTest(BasicController.class)
@Import(TestSecurityConfig.class)
class BasicControllerTest {
    @Autowired private MockMvc mvc;
//    @Autowired ObjectMapper objectMapper;
    @MockBean private BasicAPIService bas;

//    @Autowired
//    public BasicControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
//        this.mvc = mockMvc;
//        this.objectMapper = objectMapper;
//    }

    @DisplayName("[GET] 사용자 페이지 검색 - 정상 검색시 표준 뷰 페이지 전달.")
    @Test
    public void givenURLAndQueryParams_whenClientGETRequest_thenSendViewPage() throws Exception {
        String puuid = "-O2mxHCCLutqV-VC6FZzTTDDDF-QlfsGlR9qP7Cwb4E7ujIzdRhrtM5ibhPlXshnx3ehrbxD01crbQ";

        //given
        String gameName = "롤찍먹만할게요";
        String tagLine = "5103";

        given(bas.findUserAccount(any())).willReturn(AccountDto.Response.of(puuid, gameName, tagLine));

        //when & then
        mvc.perform(
                get("/user")
                        .queryParam("gameName", gameName)
                        .queryParam("tagLine", tagLine)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("users/롤찍먹만할게요"))
                .andExpect(model().attributeExists("puuid", "gameName","tagLine"))
                .andDo(print());

        then(bas).should().findUserAccount(any());
    }

    @DisplayName("[GET] 잘못된 헤더 정보 포함 - 잘못된 쿼리 파라미터 전달 시 APIErrorResponse 전달")
    @Test
    public void givenWrongQuestParams_whenClientGETRequest_thenSendAPIErrorResponse() throws Exception {

    }

    @DisplayName("[GET] 잘못된 url 검색 - 잘못된 요청 시 에러 뷰 페이지 전달")
    @Test
    public void givenWrongURL_whenClientGETRequest_thenSendErrorViewPage() throws Exception {
        mvc.perform(
                get("/user_error_url")
                    .accept(MediaType.TEXT_HTML)
                    .queryParam("gameName", "롤찍먹만할게요")
                    .queryParam("tagLine", "5103")
        )
                .andExpect(view().name("error"));
    }
}