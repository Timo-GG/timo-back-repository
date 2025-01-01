package com.tools.seoultech.timoproject.service.postService;

import com.tools.seoultech.timoproject.constant.ErrorCode;
import com.tools.seoultech.timoproject.domain.Post;
import com.tools.seoultech.timoproject.dto.APIErrorResponse;
import com.tools.seoultech.timoproject.dto.PageDTO;
import com.tools.seoultech.timoproject.dto.PostDTO;
import com.tools.seoultech.timoproject.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    public PostDTO read(Long id){
        Optional<Post> post = postRepository.findById(id);
        return post.isPresent() ? entityToDto(post.get()) : null;
    }
    @Transactional
    public ResponseEntity<APIErrorResponse> create(PostDTO postDto){
        Post post = this.dtoToEntity(postDto);
        postRepository.save(post);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIErrorResponse.of(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage()+": create 성공."));
    }
    @Transactional
    public ResponseEntity<APIErrorResponse> update(PostDTO postDto){
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());
        ResponseEntity<APIErrorResponse> response;
        // 엄격한 post. > ID 조회 시 없는 경우, 해당 id로 create 하지 않고 4XX 에러.
        if(optionalPost.isPresent()){
            postRepository.save(dtoToEntity(postDto));
            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIErrorResponse.of(
                            true,
                            ErrorCode.OK
                    ));
        }
        else{
            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIErrorResponse.of(
                            false,
                            ErrorCode.BAD_REQUEST
                    ));
        }
        return response;
    }
    @Transactional
    public ResponseEntity<APIErrorResponse> delete(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        ResponseEntity<APIErrorResponse> response;
        if(optionalPost.isPresent()){
            postRepository.deleteById(id);
            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIErrorResponse.of(true, ErrorCode.OK));
        }
        else {
            response = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(APIErrorResponse.of(false, ErrorCode.BAD_REQUEST));
        }
        return response;
    }
}
