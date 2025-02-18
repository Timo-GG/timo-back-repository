package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.mapper.PostMapper;
import com.tools.seoultech.timoproject.post.repository.PostRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;
    private final PostMapper postMapper;

    public PostDTO.Response entityToDto(Post post) {
        return postMapper.postToPostDTO(post);
    }
    public Post dtoToEntity(Object postDTO) {
        Post post;
        if (postDTO instanceof PostDTO.Response response) {
            Member member = memberRepository.findById(response.memberId()).orElseThrow(() -> new GeneralException("없음."));
            post = postMapper.postDtoToPost(response, member);
        }
        else if(postDTO instanceof PostDTO.Request request){
            Member member = memberRepository.findById(request.memberId()).orElseThrow(() -> new GeneralException("없음."));
            post = postMapper.postDtoToPost(request, member);
        }
        else {
            throw new GeneralException("");
        }
        return post;
    }

    public PageDTO.Response<PostDTO.Response, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO.Response> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    @Transactional
    public PostDTO.Response read(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.increaseViewCount();
        entityManager.merge(post);
        return entityToDto(post);
    }
    @Transactional
    public List<PostDTO.Response> readAll(){
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::entityToDto).toList();
    }
    @Transactional
    public PostDTO.Response create(PostDTO.Request postDto){
        Post post = postRepository.save(dtoToEntity(postDto));
        return entityToDto(post);
    }
    @Transactional
    public PostDTO.Response update(Long id, PostDTO.Request request){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.updatePost(id, request);
        entityManager.merge(post);
        return entityToDto(post);
    }
    @Transactional
    public void delete(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public PostDTO.Response increaseLikeCount(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.increaseLikeCount();
        entityManager.merge(post);
        return entityToDto(post);
    }
    @Transactional
    public PostDTO.Response decreaseLikeCount(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.decreaseLikeCount();
        entityManager.merge(post);
        return entityToDto(post);
    }

    public List<PostDTO.Response> readByMember(Long memberId) {
        if(!memberRepository.existsById(memberId))
            throw new GeneralException("해당 사용자가 없습니다.");

        List<Post> postList =  postRepository.findByMemberId(memberId);
        if(postList.isEmpty())
            throw new GeneralException("해당 사용자의 게시글이 없습니다.");

        List<PostDTO.Response> postDtoList = postList.stream()
                .map(this::entityToDto)
                .toList();
        return postDtoList;
    }
}
