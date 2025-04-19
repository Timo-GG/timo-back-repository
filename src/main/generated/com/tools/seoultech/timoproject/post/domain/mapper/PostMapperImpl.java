package com.tools.seoultech.timoproject.post.domain.mapper;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-19T16:50:49+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDTO.Response postToPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO.Response.ResponseBuilder response = PostDTO.Response.builder();

        response.id( post.getId() );
        response.title( post.getTitle() );
        response.content( post.getContent() );
        response.category( post.getCategory() );
        response.viewCount( post.getViewCount() );
        response.likeCount( post.getLikeCount() );
        response.regDate( post.getRegDate() );
        response.modDate( post.getModDate() );

        response.memberId( post.getMember().getId() );
        response.imageCount( post.getImages().size() );
        response.commentCount( post.getComments().size() );
        response.memberName( post.getMember().getNickname() );

        return response.build();
    }

    @Override
    public Post postDtoToPost(PostDTO.Response response, Member member) {
        if ( response == null && member == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( response != null ) {
            post.title( response.title() );
            post.content( response.content() );
            post.category( response.category() );
            post.viewCount( response.viewCount() );
            post.likeCount( response.likeCount() );
        }
        if ( member != null ) {
            post.member( member );
            List<Comment> list = member.getComments();
            if ( list != null ) {
                post.comments( new ArrayList<Comment>( list ) );
            }
        }
        post.id( response.id() );

        return post.build();
    }

    @Override
    public Post postDtoToPost(PostDTO.Request request, Member member) {
        if ( request == null && member == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( request != null ) {
            post.title( request.title() );
            post.content( request.content() );
            post.category( request.category() );
        }
        if ( member != null ) {
            post.member( member );
            List<Comment> list = member.getComments();
            if ( list != null ) {
                post.comments( new ArrayList<Comment>( list ) );
            }
        }

        return post.build();
    }

    @Override
    public Member getMemberEntity(Long id) {
        if ( id == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        return member.build();
    }
}
