package com.tools.seoultech.timoproject.post.domain.mapper;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-28T19:34:52+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.41.0.z20250213-2037, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDTO.Response commentToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDTO.Response.ResponseBuilder response = CommentDTO.Response.builder();

        response.content( comment.getContent() );
        response.id( comment.getId() );
        response.modDate( comment.getModDate() );
        response.regDate( comment.getRegDate() );

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
