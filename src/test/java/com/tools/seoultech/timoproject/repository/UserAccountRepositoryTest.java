package com.tools.seoultech.timoproject.repository;

import com.tools.seoultech.timoproject.domain.UserAccount;
import com.tools.seoultech.timoproject.dto.AccountDto;
import com.tools.seoultech.timoproject.service.BasicAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserAccountRepositoryTest {
    @Autowired private BasicAPIService bas;
    @Autowired private UserAccountRepository userAccountRepository;

    @DisplayName("[Create] 유저 생성")
    @Test
    public void create() {
        List<String> userList = Arrays.asList("롤찍먹만할게요#5103", "YORUSHlKA#KR1", "트리오브세이비어아시는구나#TOS");
        userList.stream()
                .map(this::getUserAccount)
                .forEach(userAccountRepository::save);
    }

    public UserAccount getUserAccount(String stringName) {
        StringTokenizer st = new StringTokenizer(stringName, "#");
        AccountDto.Request request = AccountDto.Request.of(st.nextToken(), st.nextToken());
        try {
            AccountDto.Response response = bas.findUserAccount(request);
            return UserAccount.builder()
                    .puuid(response.getPuuid())
                    .gameName(response.getGameName())
                    .tagLine(response.getTagLine())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}