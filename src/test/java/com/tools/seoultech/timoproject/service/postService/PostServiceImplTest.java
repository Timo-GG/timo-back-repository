package com.tools.seoultech.timoproject.service.postService;

import com.tools.seoultech.timoproject.repository.PostRepository;
import com.tools.seoultech.timoproject.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Post ")
@SpringBootTest
class PostServiceImplTest {
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired UserAccountRepository userAccountRepository;

    @DisplayName("[CREATE] 정상적인 게시글 생성 - Repository에 저장이 된다.")
    @Test
    public void given_when_then_create(){}

    @DisplayName("[UPDATE] 정상적인 게시글 업데이트 - Repository에 존재하는 게시글을 수정한다.")
    @Test
    public void given_when_then_update(){}

    @DisplayName("[READ] 정상적인 게시글 읽기 - Repository에 있는 Post를 가져온다.")
    @Test
    public void given_when_then_read(){}

    @DisplayName("[DELETE] 정상적인 게시글 삭제 - Repository에 있는 Post를 삭제한다.")
    @Test
    public void given_when_then_delete(){}

}