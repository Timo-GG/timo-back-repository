package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.mapper.PostMapper;
import com.tools.seoultech.timoproject.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final MemberRepository memberRepository;
    private final PostMapper postMapper;

    public PostDTO entityToDto(Post post) {
        return postMapper.postToPostDTO(post);
    }
    public Post dtoToEntity(PostDTO postDTO) {
        Member member = memberRepository.findById(postDTO.memberId()).orElseThrow(() -> new GeneralException("없음."));
        return postMapper.postDTOToPost(postDTO, member);
    }
    public Post dtoRequestToEntity(PostDtoRequest postDTO) {
        Member member = memberRepository.findById(postDTO.memberId()).orElseThrow(() -> new GeneralException("없음."));
        return postMapper.postDTORequestToPost(postDTO, member);
    }
    public PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    public PostDTO read(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new GeneralException(ErrorCode.BAD_REQUEST));
        return entityToDto(post);
    }
    public List<PostDTO> readAll(){
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::entityToDto).toList();
    }
    @Transactional
    public PostDTO create(PostDtoRequest postDto){
        Post post = postRepository.save(dtoRequestToEntity(postDto));
        return entityToDto(post);
    }
    @Transactional
    public PostDTO create(PostDTO postDto){
        Post post = this.dtoToEntity(postDto);
        postRepository.save(post);
        return entityToDto(post);
    }
    @Transactional
    public PostDTO update(PostDTO postDto){
        Optional<Post> optionalPost = postRepository.findById(postDto.id());

        //TODO: post에 updatePost 메서드 추가 필요. 지금에서는 BaseEntity가 새로 생성되서 안됨.
        if(optionalPost.isPresent()){
            Post newPost = Post.builder()
                    .id(postDto.id())
                    .title(postDto.title())
                    .content(postDto.content())
                    .member(memberRepository.findById(postDto.memberId()).get())
                    .build();
            return entityToDto(postRepository.save(newPost));
        }
        else{
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

    }
    @Transactional
    public void delete(Long id){
        postRepository.deleteById(id);
    }
}
