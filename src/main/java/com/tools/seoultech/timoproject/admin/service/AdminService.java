package com.tools.seoultech.timoproject.admin.service;

public interface AdminService {
    /**
     * 아이디와 비밀번호를 검증하는 메서드
     *
     * @param username 입력받은 아이디
     * @param password 입력받은 비밀번호
     * @return 인증 성공 여부
     */
    boolean authenticate(String username, String password);
}
