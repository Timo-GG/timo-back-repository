package com.tools.seoultech.timoproject.post.domain.mapper;


import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target="memberId", expression = "java(post.getMember().getId())")
    @Mapping(target="imageCount", expression = "java(post.getImages().size())")
    @Mapping(target="commentCount", expression = "java(post.getComments().size())")
    @Mapping(target="memberName", expression = "java(post.getMember().getNickname())")
    PostDTO.Response postToPostDTO(Post post);

    @Mapping(target="member", source="member")
    @Mapping(target="id", expression="java(response.id())")
    Post postDtoToPost(PostDTO.Response response, Member member);

    @Mapping(target="member", source="member")
    @Mapping(target="id", ignore=true)
    Post postDtoToPost(PostDTO.Request request, Member member);

    @Named("getMemberEntity")
    Member getMemberEntity(Long id);
        // abstract 클래스에 상속해서 빈 주입.
        // 서비스 레이어에서 파라미터 주입.

}
