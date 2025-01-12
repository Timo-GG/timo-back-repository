package com.tools.seoultech.timoproject.post.domain.mapper;


import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.domain.entity.UserAccount;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target="puuid", expression = "java(post.getUserAccount().getPuuid())")
    PostDTO postToPostDTO(Post post);

    @Mapping(target="userAccount", source="userAccount")
    Post postDTOToPost(PostDTO postDTO, UserAccount userAccount);

    @Mapping(target="userAccount", source="userAccount")
    Post postDTORequestToPost(PostDtoRequest postDtoRequest, UserAccount userAccount);

    @Named("getAccountUserEntity")
    UserAccount getAccountUserEntity(String puuid);
        // abstract 클래스에 상속해서 빈 주입.
        // 서비스 레이어에서 파라미터 주입.

}
