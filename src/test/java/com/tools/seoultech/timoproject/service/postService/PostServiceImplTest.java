package com.tools.seoultech.timoproject.service.postService;

import com.tools.seoultech.timoproject.constant.ErrorCode;
import com.tools.seoultech.timoproject.domain.Post;
import com.tools.seoultech.timoproject.dto.APIErrorResponse;
import com.tools.seoultech.timoproject.dto.AccountDto;
import com.tools.seoultech.timoproject.dto.PostDTO;
import com.tools.seoultech.timoproject.repository.PostRepository;
import com.tools.seoultech.timoproject.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("Post")
@SpringBootTest
class PostServiceImplTest {
    @Autowired PostServiceImpl postService;
    @Autowired PostRepository postRepository;
    @Autowired UserAccountRepository userAccountRepository;

    @Value("${my_puuid}") private String my_puuid;

    @DisplayName("[CREATE] 정상적인 게시글 생성 - Repository에 저장이 된다.")
    @Test
    public void givenPostDto_whenCreate_thenCreate(){
        PostDTO postDto = PostDTO.builder()
                .id(any())
                .title("[test]PostServiceImplTest...")
                .content("Posted Created in PostServiceImpl Layer")
                .puuid(my_puuid)
                .build();
    }

    @DisplayName("[UPDATE] 정상적인 게시글 업데이트 - Repository에 존재하는 게시글을 수정한다.")
    @Test
    public void givenPostDto_whenRequestUpdate_thenUpdate(){
        PostDTO postDto = PostDTO.builder()
                .title("[test]PostServiceImplTest... >>> UpdatePostServiceImplTest...")
                .content("Post Updated in PostServiceImpl Layer")
                .puuid(my_puuid)
                .build();
//        postService.update(postDto);
    }
    @DisplayName("[UPDATE] 비정상적인 게시글 업데이트 - Repository에 등록되지 않은 사용자의 게시글 수정 요청.")
    @Test
    public void givenPostDto_whenRequestUpdate_thenResponseErrorAPIResponse(){
        PostDTO postDto = PostDTO.builder()
                .title("[test]WrongAccount....")
                .content("Wrong user request...")
                .puuid(null)
                .build();
//        postService.update(postDto);
    }
    @DisplayName("[READ] 정상적인 게시글 읽기 - Repository에 있는 Post를 가져온다.")
    @Test
    public void givenPostId_whenRequestRead_thenRead(){
        Long id = any();
        Post post = postRepository.findById(id).get();
        System.out.println(post.toString());
    }

    @DisplayName("[DELETE] 정상적인 게시글 삭제 - Repository에 있는 Post를 삭제한다.")
    @Test
    public void givenPostId_whenRequestDelete_thenDelete(){

    }

}