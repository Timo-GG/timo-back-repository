package com.tools.seoultech.timoproject.service.postService;


import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.post.domain.mapper.PostMapper;
import com.tools.seoultech.timoproject.post.repository.PostRepository;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("Post")
class PostServiceImplTest {
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PostMapper postMapper;
    @InjectMocks
    PostServiceImpl postService;

    String my_puuid="-O2mxHCCLutqV-VC6FZzTTDDDF-QlfsGlR9qP7Cwb4E7ujIzdRhrtM5ibhPlXshnx3ehrbxD01crbQ";

    @Test
    @DisplayName("[CREATE] 정상적인 게시글 생성 - Repository에 저장이 된다.")
    @Transactional
    public void givenPostDto_whenCreate_thenCreate(){
        //given
        Member member = Member.builder()
                .email("louis38625103@gmail.com")
                .username("롤찍먹만할게요")
                .build();

        PostDTO postDto = PostDTO.builder()
                .title("PostService test")
                .content("test content...")
                .memberId(1L)
                .category(Category.CREATIVITY)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();


        PostDTO savedPostDTO = PostDTO.builder()
                .id(1L)
                .title("PostService test")
                .content("test content...")
                .memberId(1L)
                .category(Category.CREATIVITY)
                .regDate(LocalDateTime.now())
                .modDate(LocalDateTime.now())
                .build();


        Post post = Post.builder()
                .id(1L)
                .title(postDto.title())
                .content(postDto.content())
                .member(member)
                .category(Category.CREATIVITY)
                .build();

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        given(postMapper.postDTOToPost(postDto, member)).willReturn(post);
        given(postRepository.save(post)).willReturn(post);
        given(postMapper.postToPostDTO(post)).willReturn(savedPostDTO);

        // when
        PostDTO savedPost = postService.create(postDto);

        // then
        assertThat(savedPost)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "PostService test")
                .hasFieldOrPropertyWithValue("content", "test content...");

        then(memberRepository).should().findById(1L);
        then(postMapper).should().postDTOToPost(postDto, member);
        then(postRepository).should().save(post);
        then(postMapper).should().postToPostDTO(post);
    }

    @DisplayName("[UPDATE] 정상적인 게시글 업데이트 - Repository에 존재하는 게시글을 수정한다.")
    @Test
    public void givenPostDto_whenRequestUpdate_thenUpdate(){
        PostDTO postDto = PostDTO.builder()
                .title("[test]PostServiceImplTest... >>> UpdatePostServiceImplTest...")
                .content("Post Updated in PostServiceImpl Layer")
                .memberId(1L)
                .build();
//        postService.update(postDto);
    }
    @DisplayName("[UPDATE] 비정상적인 게시글 업데이트 - Repository에 등록되지 않은 사용자의 게시글 수정 요청.")
    @Test
    public void givenPostDto_whenRequestUpdate_thenResponseErrorAPIResponse(){
        PostDTO postDto = PostDTO.builder()
                .title("[test]WrongAccount....")
                .content("Wrong user request...")
                .memberId(null)
                .build();
//        postService.update(postDto);
    }
    @DisplayName("[READ] 정상적인 게시글 읽기 - Repository에 있는 Post를 가져온다.")
    @Test
    public void givenPostId_whenRequestRead_thenRead(){
        Post cpost = Post.builder().build();
        Long id = any();
        Post post = postRepository.findById(id).orElseGet(() -> cpost);
        System.out.println(post.toString());
    }

    @DisplayName("[DELETE] 정상적인 게시글 삭제 - Repository에 있는 Post를 삭제한다.")
    @Test
    public void givenPostId_whenRequestDelete_thenDelete(){

    }

}