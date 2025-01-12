package com.tools.seoultech.timoproject.post.service;

<<<<<<< HEAD
import com.tools.seoultech.timoproject.post.domain.Post;
import com.tools.seoultech.timoproject.post.dto.PageDTO;
import com.tools.seoultech.timoproject.post.dto.PostDTO;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
=======
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.post.domain.dto.PostDtoRequest;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.UserAccount;
import com.tools.seoultech.timoproject.post.domain.mapper.PostMapper;
import com.tools.seoultech.timoproject.post.repository.PostRepository;

import com.tools.seoultech.timoproject.post.repository.UserAccountRepository;
>>>>>>> #12-crud-repository
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
<<<<<<< HEAD

=======
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
>>>>>>> #12-crud-repository
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
<<<<<<< HEAD

=======
    private final UserAccountRepository userAccountRepository;
    private final PostMapper postMapper;

    public PostDTO entityToDto(Post post) {
        return postMapper.postToPostDTO(post);
    }
    public Post dtoToEntity(PostDTO postDTO) {
        UserAccount userAccount = userAccountRepository.findById(postDTO.getPuuid()).orElseThrow(() -> new GeneralException("없음."));
        return postMapper.postDTOToPost(postDTO, userAccount);
    }
    public Post dtoRequestToEntity(PostDtoRequest postDTO) {
        UserAccount userAccount = userAccountRepository.findById(postDTO.getPuuid()).orElseThrow(() -> new GeneralException("없음."));
//        return Post.builder().title(postDTO.getTitle()).content(postDTO.getContent()).userAccount(userAccount).build();
        return postMapper.postDTORequestToPost(postDTO, userAccount);
    }
>>>>>>> #12-crud-repository
    public PageDTO.Response<PostDTO, Post> getList(PageDTO.Request request){
        Pageable pageable = request.getPageable(Sort.by("id").descending());
        Page<Post> result = postRepository.findAll(pageable);
        Function<Post, PostDTO> fn = this::entityToDto;
        return PageDTO.Response.of(result, fn);
    }
    public PostDTO read(Long id){
<<<<<<< HEAD
        Optional<Post> post = postRepository.findById(id);
        return post.isPresent() ? entityToDto(post.get()) : null;
=======
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
        Optional<Post> optionalPost = postRepository.findById(postDto.getId());

        if(optionalPost.isPresent()){
            Post newPost = Post.builder()
                    .id(postDto.getId())
                    .title(postDto.getTitle())
                    .content(postDto.getContent())
                    .userAccount(userAccountRepository.findById(postDto.getPuuid()).get())
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
>>>>>>> #12-crud-repository
    }
}
