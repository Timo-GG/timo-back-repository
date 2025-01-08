package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.post.domain.Post;
import com.tools.seoultech.timoproject.post.dto.PageDTO;
import com.tools.seoultech.timoproject.post.dto.PostDTO;
import com.tools.seoultech.timoproject.post.repository.UserAccountRepository;


public interface PostService {
    PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request);
    PostDTO read(Long id);

    default PostDTO entityToDto(Post entity){
        // ObjectMapper 사용하면 되는 부분 아닌가?
        PostDTO dto = PostDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .puuid(entity.getUserAccount().getPuuid())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
    default Post dtoToEntity(PostDTO dto, UserAccountRepository userAccountRepository){
        Post entity = Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .userAccount(userAccountRepository.findById(dto.getPuuid()).get()) // exception 필요.
                .build();
        return entity;
    }
}
