package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyPageMapper {
    MyPageMapper Instance = Mappers.getMapper(MyPageMapper.class);

    @Mappings({
            @Mapping(target = "board", source = "redisBoard"),
            @Mapping(target = "requestorMemberId", source = "requestorMemberId"),
            @Mapping(target = "acceptorMemberId", expression = "java(redisBoard.getRedisUser().getMemberId())"),
            @Mapping(target = "matchingCategory", expression = "java(redisBoard.getMatchingCategory())"),
    })
    RedisMyPage toRedisMyPage(RedisBoard redisBoard, Long requestorMemberId);

    @Mapping(target = "acceptorBoard", source = ".", qualifiedByName = "boardDtoMapping")
    @Mapping(target = "requestor", source = ".", qualifiedByName = "userDtoMapping")
    @Mapping(target = "myPageUUID", source = "uuid")
    MyPageDTO.ResponseMyPage toDtoFromRedis(RedisMyPage redisMyPage);

    @Named("boardDtoMapping")
    static BoardDTO.Response boardDtoMapping(RedisMyPage redisMyPage){
        if(redisMyPage.getMatchingCategory() == MatchingCategory.DUO)
            return BoardMapper.Instance.toResponseDuo((RedisBoard.Duo)redisMyPage.getBoard());
        else
            return BoardMapper.Instance.toResponseColosseum((RedisBoard.Colosseum)redisMyPage.getBoard());
    }

    @Named("userDtoMapping")
    static UserDTO.Response userDtoMapping(RedisMyPage redisMyPage){
        if(redisMyPage.getMatchingCategory() == MatchingCategory.DUO)
            return UserMapper.Instance.toResponseDuo((RedisUser.Duo)redisMyPage.getRequestor());
        else
            return UserMapper.Instance.toResponseColosseum((RedisUser.Colosseum)redisMyPage.getRequestor());
    }
}
