package com.tools.seoultech.timoproject.post.domain.mapper;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target="memberId", expression = "java(comment.getMember().getId())")
    @Mapping(target="postId", expression = "java(comment.getPost().getId())")
    CommentDTO.Response commentToCommentDto(Comment comment);

    @Mapping(target="member", source="member")
    @Mapping(target="post", source="post")
    @Mapping(target="id", expression="java(commentDTO.id())")
    @Mapping(target="content", expression="java(commentDTO.content())")
    Comment commentDtoToComment(CommentDTO.Response commentDTO, Member member, Post post);

    @Mapping(target="member", source="member")
    @Mapping(target="post", source="post")
    @Mapping(target="id", ignore = true)
    @Mapping(target="content", expression="java(commentDTO.content())")
    Comment commentDtoToComment(CommentDTO.Request commentDTO, Member member, Post post);
}
