package com.tools.seoultech.timoproject.version2.matching.user.dto;

import com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDTO {
    private final MatchingCategory matchingCategory;
    private final Object dto;

    @Builder
    public record RequestDuoUser(){}

    @Builder
    public record RequestColosseumUser(){}

    @Builder
    public record ResponseDuoUser(){}

    @Builder
    public record ResponseColloseumUser(){}

    public UserDTO of(MatchingCategory matchingCategory, RequestDuoUser dto){
        return new UserDTO(matchingCategory, dto);
    }
    public UserDTO of(MatchingCategory matchingCategory, ResponseDuoUser dto){
        return new UserDTO(matchingCategory, dto);
    }
    public UserDTO of(MatchingCategory matchingCategory, RequestColosseumUser dto){
        return new UserDTO(matchingCategory, dto);
    }
    public UserDTO of(MatchingCategory matchingCategory, ResponseColloseumUser dto){
        return new UserDTO(matchingCategory, dto);
    }
}
