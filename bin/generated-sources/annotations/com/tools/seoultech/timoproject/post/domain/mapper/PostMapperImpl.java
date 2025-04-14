package com.tools.seoultech.timoproject.post.domain.mapper;

import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-28T19:34:52+0900",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.41.0.z20250213-2037, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDTO.Response postToPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO.Response.ResponseBuilder response = PostDTO.Response.builder();

        response.category( post.getCategory() );
        response.content( post.getContent() );
        response.id( post.getId() );
        response.likeCount( post.getLikeCount() );
        response.modDate( post.getModDate() );
        response.regDate( post.getRegDate() );
        response.title( post.getTitle() );
        response.viewCount( post.getViewCount() );

        response.memberId( post.getMemberId() );

        return response.build();
    }

    @Override
    public Post postDtoToPost(PostDTO.Response response, Member member) {
        if ( response == null && member == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        if ( response != null ) {
            post.category( response.category() );
            post.content( response.content() );
            post.likeCount( response.likeCount() );
            post.title( response.title() );
            post.viewCount( response.viewCount() );
        }
        post.memberId( member.getId() );
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
            post.category( request.category() );
            post.content( request.content() );
            post.title( request.title() );
        }
        post.memberId( member.getId() );

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
