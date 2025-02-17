package com.tools.seoultech.timoproject.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public Long getId() {
        return member.getId();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getNickname() {
        return member.getNickname();
    }

    public Role getRole() {
        return member.getRole();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "none";
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return member.getRole().toString();
            }
        });

        return collection;
    }
}
