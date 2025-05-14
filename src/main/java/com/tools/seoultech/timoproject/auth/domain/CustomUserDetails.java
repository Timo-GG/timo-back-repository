package com.tools.seoultech.timoproject.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public static CustomUserDetails from (Member member) {
        return new CustomUserDetails(member);
    }

    public Long getMemberId() {
        return member.getMemberId();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
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
