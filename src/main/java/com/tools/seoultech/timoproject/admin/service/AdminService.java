package com.tools.seoultech.timoproject.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    /**
     * 아이디와 비밀번호를 검증하는 메서드
     *
     * @param username 입력받은 아이디
     * @param password 입력받은 비밀번호
     * @return 인증 성공 여부
     */
    public boolean authenticate(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }
}
