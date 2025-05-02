package com.tools.seoultech.timoproject.version2.matching.service.factory;

import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RedisKeyFactory {
    public static String UserKey(){
        // Key : User:324325
        return String.format("User:%s", UUID.randomUUID());
    }
    public static String UserSetKeyBy(MatchingCategory category){
        // Key : User:Duo
        // Key : User:Colosseum
        return String.format("User:%s", category.toString());
    }
    public static String UserSetKeyBy(Long memberId){
        // Key : User:Member:1L
        return String.format("User:%s:%d", "Member", memberId);
    }
//    public static List<String> getKeySetList(UserDTO requestDto){
//        String categoryKey = RedisKeyFactory.UserSetKeyBy(requestDto.getMatchingCategory());
//        String memberKey = RedisKeyFactory.UserSetKeyBy(requestDto.getMemberId());
//        return List.of(categoryKey, memberKey);
//    }
}
