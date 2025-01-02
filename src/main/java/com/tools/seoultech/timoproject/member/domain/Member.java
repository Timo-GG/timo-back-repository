package com.tools.seoultech.timoproject.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    protected Member() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 식별자 : Provider + " " + ProviderId
     * 예시 : naver JfgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUWLzaw4
     */
    private String username;

    private String email;

    private String role;

    public static Member create(String username, String email, String role) {
        return new Member(username, email, role);
    }

    private Member(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public void update(String email) {
        this.email = email;
    }
}
