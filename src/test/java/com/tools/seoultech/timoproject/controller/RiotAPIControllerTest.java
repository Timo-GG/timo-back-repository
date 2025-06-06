package com.tools.seoultech.timoproject.controller;

import com.tools.seoultech.timoproject.global.config.TestSecurityConfig;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.error.APIExceptionHandler;
import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.global.exception.RiotAPIException;
import com.tools.seoultech.timoproject.riot.controller.RiotAPIController;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
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


@DisplayName("[API Controller]")
@WebMvcTest(RiotAPIController.class)
@ContextConfiguration(classes = {RiotAPIController.class, APIExceptionHandler.class})
@Import(TestSecurityConfig.class)
class RiotAPIControllerTest {
    @Autowired MockMvc mvc;
//    @Autowired private final ObjectMapper objectMapper;

    @MockBean private RiotAPIService bas;

//    @Autowired
//    public BasicAPIControllerTest(
//             MockMvc mockMvc,
//             ObjectMapper objectMapper) {
//        this.mvc = mockMvc;
//        this.objectMapper = objectMapper;
//    }

    @DisplayName("[GET] puuid 검색 - 정상 검색 시 표준 APIDataResponse 출력.")
    @Test
    public void givenAccountDtoRequestJsonBody_whenSearchingUserAccount_thenReturnAPIDataResponse() throws Exception{
        String puuid = "-O2mxHCCLutqV-VC6FZzTTDDDF-QlfsGlR9qP7Cwb4E7ujIzdRhrtM5ibhPlXshnx3ehrbxD01crbQ";

        //given
        String gameName = "롤찍먹만할게요";
        String tagLine = "5103";

        AccountDto.Request request_dto = AccountDto.Request.of(gameName, tagLine);
        given(bas.findUserAccount(any())).willReturn(AccountDto.Response.of(puuid, gameName, tagLine));

        // when & then
        mvc.perform(
                        get("/api/request/Account")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request_dto))
                                .queryParam("gameName", gameName)
                                .queryParam("tagLine", tagLine)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.data.puuid").value(puuid))
                .andDo(print());
        then(bas).should().findUserAccount(any());
    }

    @DisplayName("[GET] puuid 검색 - 잘못된 요청 시 표준 APIErrorResponse 출력.")
    @Test
    public void givenWrongUserAccountDtoRequestJsonBody_whenSearchingUserAccount_thenReturnAPIErrorResponse() throws Exception{
        // given
        String gameName = "test_wrong_id@$";
        String tagLine = "test_wrong_tag";
        AccountDto.Request request_dto = AccountDto.Request.of(gameName, tagLine);
        given(bas.findUserAccount(any()))
                .willThrow(new RiotAPIException("계정 정보 API 호출 실패 - 사용자 정보가 없습니다.", ErrorCode.API_ACCESS_ERROR));

        // when & then
        mvc.perform(
                get("/api/request/Account")
                        .queryParam("gameName", gameName)
                        .queryParam("tagLine", tagLine)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.API_ACCESS_ERROR.getCode()))
                .andDo(print());
        then(bas).should().findUserAccount(any());
    }
}

