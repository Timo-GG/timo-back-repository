package com.tools.seoultech.timoproject.post.domain.mapper;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-19T16:50:49+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDTO.Response commentToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDTO.Response.ResponseBuilder response = CommentDTO.Response.builder();

        response.id( comment.getId() );
        response.content( comment.getContent() );
        response.regDate( comment.getRegDate() );
        response.modDate( comment.getModDate() );

        response.memberId( comment.getMember().getId() );
        response.postId( comment.getPost().getId() );

        return response.build();
    }

    @Override
    public Comment commentDtoToComment(CommentDTO.Response commentDTO, Member member, Post post) {
        if ( commentDTO == null && member == null && post == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.member( member );
        comment.post( post );
        comment.id( commentDTO.id() );
        comment.content( commentDTO.content() );

        return comment.build();
    }

    @Override
    public Comment commentDtoToComment(CommentDTO.Request commentDTO, Member member, Post post) {
        if ( commentDTO == null && member == null && post == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.member( member );
        comment.post( post );
        comment.content( commentDTO.content() );

        return comment.build();
    }
}
