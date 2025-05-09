package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyPageMapper {

    UserMapper myPageMapperInstance = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "board", source = "redisBoard"),
            @Mapping(target = "requestorMemberId", source = "requestorMemberId"),
            @Mapping(target = "acceptorMemberId", expression = "java(redisBoard.getRedisUser().getMemberId())"),
            @Mapping(target = "matchingCategory", expression = "java(redisBoard.getMatchingCategory())"),
    })
    RedisMyPage toRedisMyPage(RedisBoard redisBoard, Long requestorMemberId);

    @Mapping(target = "myPageUUID", source = "uuid")
    MyPageDTO.ResponseMyPage toDtoFromRedis(RedisMyPage redisMyPage);

}
