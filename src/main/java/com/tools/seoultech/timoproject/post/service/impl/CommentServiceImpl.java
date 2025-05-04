package com.tools.seoultech.timoproject.post.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.dto.Comment_SearchingFilterDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.entity.QComment;
import com.tools.seoultech.timoproject.post.domain.mapper.CommentMapper;
import com.tools.seoultech.timoproject.post.repository.CommentRepository;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
import com.tools.seoultech.timoproject.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentMapper mapper;

    @Override
    public Comment dtoToEntity(Object dto) {
        if (Objects.isNull(dto))
            throw new GeneralException("해당 dto가 Null 값입니다.");

        Comment comment;
        if(dto instanceof CommentDTO.Request requestDto) {
            Member member = memberRepository.findById(requestDto.memberId()).
                    orElseThrow( () -> new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다."));

            Post post = postRepository.findById(requestDto.postId())
                    .orElseThrow( () -> new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다."));

            comment = mapper.commentDtoToComment(requestDto, member, post);
        }
        else if(dto instanceof CommentDTO.Response responseDto){
            Member member = memberRepository.findById(responseDto.memberId()).
                    orElseThrow( () -> new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다.")
            );
            Post post = postRepository.findById(responseDto.postId())
                    .orElseThrow(() -> new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다."));
            comment = mapper.commentDtoToComment(responseDto, member, post);
        }
        else {
            throw new IllegalArgumentException("Dto 타입 불일치.");
        }
        return comment;
    }

    @Override
    public CommentDTO.Response entityToDto(Comment comment) {
        if(Objects.isNull(comment))
            throw new GeneralException("해당 Comment 엔티티 객체가 Null값입니다.");

        CommentDTO.Response responseDto = mapper.commentToCommentDto(comment);
        return responseDto;
    }

    @Override
    public Comment create(CommentDTO.Request dto) {
//        Comment comment = dtoToEntity(dto);
        Comment comment = commentRepository.save(dtoToEntity(dto));
        return comment;
    }

    @Override
    public Comment read(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new GeneralException("1"));
        return comment;
    }

    @Override
    @Transactional
    public Comment update(Long id, CommentDTO.Request requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new GeneralException("1"));
        comment.updateComment(requestDto);
        return comment;
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> searchCommentByFilter(Comment_SearchingFilterDTO filterDto, Pageable pageable) {
        BooleanBuilder builder = searchFilterValidation(filterDto);
        Sort sort = (filterDto.sortOrder()) ? Sort.by(Sort.Order.asc(filterDto.sortBy())) : Sort.by(Sort.Order.desc(filterDto.sortBy()));
        List<Comment> commentList = commentRepository.findAll(
                builder,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        sort)
        ).getContent();
        return commentList;
    }
    private BooleanBuilder searchFilterValidation(Comment_SearchingFilterDTO filterDto) {
        QComment qComment = QComment.comment;
        BooleanBuilder builder = new BooleanBuilder();

        if(Objects.nonNull(filterDto.commentId())) {
            boolean exists = commentRepository.existsById(filterDto.commentId());
            if(!exists)
                throw new GeneralException("해당 commentId는 존재하지 않는 Id 값입니다.");
            builder.and(qComment.id.eq(filterDto.commentId()));
        }
        if(Objects.nonNull(filterDto.postId())) {
            boolean exists = postRepository.existsById(filterDto.postId());
            if(!exists)
                throw new GeneralException("해당 postId는 존재하지 않는 Id 값입니다.");
            builder.and(qComment.post.id.eq(filterDto.postId()));
        }
        if(Objects.nonNull(filterDto.memberId())) {
            boolean exists = memberRepository.existsById(filterDto.memberId());
            if(!exists)
                throw new GeneralException("해당 memberId는 존재하지 않는 Id 값입니다.");
            builder.and(qComment.member.id.eq(filterDto.memberId()));
        }
        return builder;
    }
}
