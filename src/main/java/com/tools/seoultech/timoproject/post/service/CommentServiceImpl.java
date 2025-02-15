package com.tools.seoultech.timoproject.post.service;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.mapper.CommentMapper;
import com.tools.seoultech.timoproject.post.repository.CommentRepository;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
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
                    orElseThrow( () ->
                            new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다.")
                    );
            Post post = postRepository.findById(requestDto.postId())
                    .orElseThrow( () ->
                            new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다.")
                    );
            comment = mapper.commentDtoToComment(requestDto, member, post);
        }
        else if(dto instanceof CommentDTO.Response responseDto){
            Member member = memberRepository.findById(responseDto.memberId()).
                    orElseThrow( () ->
                            new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다.")
            );
            Post post = postRepository.findById(responseDto.postId())
                    .orElseThrow(() ->
                            new GeneralException("Member Repository에 해당 Id 값을 가진 엔티티가 없습니다.")
                    );
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
    public CommentDTO.Response create(CommentDTO.Request dto) {
        Comment comment = dtoToEntity(dto);
        comment = commentRepository.save(comment);
        return entityToDto(comment);
    }

    @Override
    public CommentDTO.Response read(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () -> new GeneralException("1"));
        return entityToDto(comment);
    }

    @Override
    public List<CommentDTO.Response> readAll() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO.Response> dtoList = comments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public CommentDTO.Response update(Long id, CommentDTO.Request requestDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow( () ->
                        new GeneralException("1")
                );
        comment.updateComment(requestDto);
        return entityToDto(comment);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
