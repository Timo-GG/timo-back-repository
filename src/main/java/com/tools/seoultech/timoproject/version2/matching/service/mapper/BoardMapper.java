package com.tools.seoultech.timoproject.version2.matching.service.mapper;


import com.tools.seoultech.timoproject.version2.matching.domain.board.dto.SearchBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(target="dto", qualifiedByName = "toResponseDuo")
    SearchBoardDTO<SearchBoardDTO.ResponseDuoBoard> dtoToRedis(
            SearchBoardDTO<SearchBoardDTO.RequestDuoBoard> requestDto,
            @Context UserMapper userMapper
    );

    @Mapping(target="dto", qualifiedByName = "toResponseColosseum")
    SearchBoardDTO<SearchBoardDTO.ResponseColosseumBoard> dtoToRedis(
            SearchBoardDTO<SearchBoardDTO.RequestColosseumBoard> requestDto
    );

//    SearchBoardDTO redisToEntity(Redis_BaseSearchBoard requestDto);

    @Named(value = "toResponseDuo")
    default SearchBoardDTO.ResponseDuoBoard toResponseDuo(
            SearchBoardDTO.RequestDuoBoard requestDto,
            @Context UserMapper userMapper,
            @Context UserService userService
    ) {
        UserDTO<UserDTO.ResponseDuoUser> responseUserDto = userMapper.dtoToRedis(requestDto.requestUserDto());
        return SearchBoardDTO.ResponseDuoBoard.builder()
                .responseUserDto(responseUserDto).build();
    }
    @Named(value = "toResponseColosseum")
    default SearchBoardDTO.ResponseColosseumBoard toResponseColosseum(
            SearchBoardDTO.RequestColosseumBoard requestDto
    ){
        return null;
    }
}
