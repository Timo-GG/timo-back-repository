package com.tools.seoultech.timoproject.post.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.dto.SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.PostLike;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.entity.QPost;
import com.tools.seoultech.timoproject.post.domain.mapper.PostMapper;
import com.tools.seoultech.timoproject.post.repository.PostLikeRepository;
import com.tools.seoultech.timoproject.post.repository.PostRepository;

import com.tools.seoultech.timoproject.post.service.PostService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final PostLikeRepository likeRepository;

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
    public Post read(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.increaseViewCount();
        entityManager.merge(post);
        return post;
    }

    @Transactional
    public List<Post> searchByFilter(
            SearchingFilterDTO filterDto,
            Pageable pageable
    ){
        BooleanBuilder builder = searchFilterValidation(filterDto.memberId(), filterDto.category());
        Sort sort = (filterDto.sortOrder()) ? Sort.by(Sort.Order.asc(filterDto.sortBy())) : Sort.by(Sort.Order.desc(filterDto.sortBy()));
        List<Post> postList = postRepository.findAll(
                builder,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        sort
                )
        ).getContent();

        return postList;
    }
    
    @Transactional
    public Post create(PostDTO.Request postDto){
        Post post = postRepository.save(dtoToEntity(postDto));
        return post;
    }
    @Transactional
    public Post update(Long id, PostDTO.Request request){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        post.updatePost(id, request);
        entityManager.merge(post);
        return post;
    }
    @Transactional
    public void delete(Long id){
        postRepository.deleteById(id);
    }

    @Transactional
    public Post increaseLikeCount(Long postId, Long memberId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("해당 postId에 해당하는 게시글이 존재하지 않습니다.", ErrorCode.BAD_REQUEST));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException("해당 memberId에 해당하는 사용자가 존재하지 않습니다.", ErrorCode.BAD_REQUEST));

        if(likeRepository.existsByMemberId(postId, memberId))
                throw new GeneralException("해당 사용자는 이미 좋아요를 눌렀습니다.", ErrorCode.BAD_REQUEST);
        else{
            PostLike like = PostLike.builder().post(post).member(member).build();
            likeRepository.save(like);

            post.increaseLikeCount();
            entityManager.merge(post);
            return post;
        }
    }
    @Transactional
    public Post decreaseLikeCount(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException("해당 postId에 해당하는 게시글이 존재하지 않습니다.", ErrorCode.BAD_REQUEST));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException("해당 memberId에 해당하는 사용자가 존재하지 않습니다.", ErrorCode.BAD_REQUEST));

        if(!likeRepository.existsByMemberId(postId, memberId))
            throw new GeneralException("해당 사용자는 좋아요를 누르지 않았습니다.", ErrorCode.BAD_REQUEST);
        else{
            likeRepository.deleteByMemberId(memberId);

            post.decreaseLikeCount();
            entityManager.merge(post);
            return post;
        }
    }

    private BooleanBuilder searchFilterValidation(
            Long memberId, Category category
    ) {
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        if(memberId != null){
            boolean isExist = memberRepository.existsById(memberId);

            if(!isExist)
                throw new GeneralException("필터링 조건에 포함된 멤버 ID에 해당하는 값이 존재하지 않습니다.");
            builder.and(post.memberId.eq(memberId));
        }
        if(category != null){
            builder.and(post.category.eq(category));
        }
        return builder;
    }
}
