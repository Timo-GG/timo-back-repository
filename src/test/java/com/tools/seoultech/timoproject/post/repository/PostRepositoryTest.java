package com.tools.seoultech.timoproject.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tools.seoultech.timoproject.global.config.QueryDSLConfig;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfig.class)
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("/test.sql")
@Rollback(true)
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void beforeEach() { // DataLoad

    }

    @Test
    public void createTest() {
        List<Post> posts = createPost();
        posts.stream()
                .forEach(post -> {
                    Post savedPost = postRepository.save(post);
                    assertTrue(Objects.nonNull(savedPost.getId()));
                    assertThat(savedPost)
                            .hasNoNullFieldsOrPropertiesExcept("comments", "likes", "images")
                            .satisfies(it -> {
                                assertThat(it.getId()).isNotNull().isNotZero();
                                assertThat(it.getTitle()).isEqualTo(post.getTitle());
                                assertThat(it.getContent()).isEqualTo(post.getContent());
                                assertThat(it.getMember()).isInstanceOf(Member.class);
                                assertThat(it.getRegDate()).isInstanceOf(LocalDateTime.class).isNotNull();
                                assertThat(it.getModDate()).isInstanceOf(LocalDateTime.class).isNotNull();
                            });
                });
    }

    @Test
    public void updateTest() throws InterruptedException {
        List<Post> posts = createPost();
        posts.stream().forEach(post -> {
            Post savedPost = postRepository.save(post);
            entityManager.flush();
            LocalDateTime beforeModDate = post.getRegDate();

            savedPost.updatePost(post.getId(),
                    PostDTO.Request.builder()
                            .title("updated Post Title...")
                            .content("updated Post Content...")
                            .memberId(post.getMember().getId())
                            .build()
            );

            Post updatedPost = postRepository.save(savedPost);
            entityManager.flush();
            LocalDateTime afterModDate = updatedPost.getModDate();

            assertTrue(Objects.nonNull(post));
            assertThat(updatedPost)
                    .hasNoNullFieldsOrPropertiesExcept("comments", "likes", "images")
                    .satisfies(it -> {
                        assertThat(it.getId())
                                .isNotNull()
                                .isNotZero()
                                .isEqualTo(savedPost.getId());
                        assertThat(it.getRegDate())
                                .isInstanceOf(LocalDateTime.class)
                                .isNotNull()
                                .isEqualTo(savedPost.getRegDate());
                        assertTrue(beforeModDate.isBefore(afterModDate));
                    });
        });

    }

    @Test
    public void deletePost() {
        // given
        List<Long> idToDelete = Arrays.asList(1L, 3L, 5L);
        List<Post> posts = postRepository.saveAll(createPost());

        idToDelete.forEach(postRepository::deleteById);
        entityManager.flush();

        idToDelete.forEach(id -> {
            Optional<Post> post = postRepository.findById(id);
            assertThat(post)
                    .satisfies(it -> {
                        assertFalse(it.isPresent());
                        assertThrows(NoSuchElementException.class, () -> post.get());
                    });
        });
    }

    private List<Post> createPost() {
        List<Post> posts = new ArrayList<>();
        IntStream.rangeClosed(1, 30).forEach(i -> {
            Post post = Post.builder()
                    .title("[test]아칼리 상향좀...")
                    .content(
                            "진짜 하 요즘 아칼리로 랭돌리기 너무 힘듬. 아칼리로 게임하면 ᄅᄋ 상대 점수 자판기 되는 느낌ᄏᄏ. 왜 이렇게 힘든지 정리해보겠음.\n" +
                            "메타랑 너무 안 맞음: 요즘 메타가 탱커랑 긴 전투가 대세라서 아칼리 기동성이나 폭딜이 ᄅᄋ 안 먹힘.\n" +
                            "탱커가 너무 단단해서 딜 넣기 힘들고, 스킬 쿨타임 길어서 빈틈 자주 생김.\n" +
                            "난이도 대비 보상 부족함: 아칼리로 플레이하기 진짜 까다롭고, 스킬 맞추기도 어렵고 위치 잘 잡아야 하는데, 보상 너무 적음. 실수 한 번 하면 게임이 흔들리기 일쑤임.\n" +
                            "다른 챔피언들한테 밀림: 요즘 카사딘, 피즈 같은 챔피언들이 너무 강력해서 아칼리가 ᄅᄋ 한없이 약해 보임.\n" +
                            "그런 챔피언들은 안정감도 좋고 딜도 훨씬 강력해서 아칼리랑 비교되면 ᄅᄋ 너무 약해 보임.\n" +
                            "진짜 이 상태로는 아칼리로 게임하기 힘듬. 밸런스 좀 조정해서 아칼리 다시 제대로 플레이할 수 있게 해줬으면 좋겠음. 반박시 니말이 다 맞음." + i)
                    .member(
                            memberRepository.findById((i%3)+1L)
                                    .orElse(
                                            Member.builder()
                                            .email("testMember" + i + "@test.com")
                                            .build())
                    ).build();
            posts.add(post);
        });
        return posts;
    }
}

