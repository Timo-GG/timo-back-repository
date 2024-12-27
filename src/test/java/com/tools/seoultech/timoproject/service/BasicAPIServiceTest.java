package com.tools.seoultech.timoproject.service;


import com.tools.seoultech.timoproject.constant.ErrorCode;
import com.tools.seoultech.timoproject.dto.AccountDto;
import com.tools.seoultech.timoproject.exception.RiotAPIException;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Riot API] 테스트")
@SpringBootTest
class BasicAPIServiceTest {
    @Autowired private BasicAPIService bas;

    @DisplayName("puuid 검색 - 공백 유저 데이터 요청")
    @Test
    public void givenNothing_whenSearchingUser_thenReturnConstraintViloationException() throws Exception {
        //given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());

        //when
        Throwable thrown = Assertions.catchThrowable(() -> bas.findUserAccount(AccountDto.Request.of(null, null)));

        //then
        assertThat(thrown)
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining(e.getMessage());
    }
    @DisplayName("puuid 검색 - 존재하지 않는 유저 데이터 요청")
    @Test
    public void givenWrongUserAccountDtoRequest_whenSearchingUser_thenReturnRiotAPIException() throws Exception {
        //given
        String gameName = "롤찍먹만할게요오";
        String tagLine = "51030";
        RiotAPIException e = new RiotAPIException("계정 정보 API 호출 실패 - 사용자 정보가 없습니다.", ErrorCode.API_ACCESS_ERROR);

        //when
        Throwable thrown = Assertions.catchThrowable(() -> bas.findUserAccount(AccountDto.Request.of(gameName, tagLine)));

        //then
        assertThat(thrown)
                .isInstanceOf(RiotAPIException.class)
                .hasMessageContaining(e.getMessage());
    }
    @DisplayName("puuid 검색 - 정상 유저 요청")
    @Test()
    public void givenAccountDtoRequest_whenSearchingUser_thenReturnAccountDtoResponse() throws Exception{
        //given
        String puuid = "-O2mxHCCLutqV-VC6FZzTTDDDF-QlfsGlR9qP7Cwb4E7ujIzdRhrtM5ibhPlXshnx3ehrbxD01crbQ";
        String name = "롤찍먹만할게요";
        String tag = "5103";

        //when
        AccountDto.Response response_dto = bas.findUserAccount(AccountDto.Request.of(name, tag));

        //then
        assertThat(response_dto)
                .hasFieldOrPropertyWithValue("puuid", puuid)
                .hasFieldOrPropertyWithValue("gameName", name)
                .hasFieldOrPropertyWithValue("tagLine", tag);
    }
}