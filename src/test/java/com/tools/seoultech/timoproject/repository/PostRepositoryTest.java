package com.tools.seoultech.timoproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tools.seoultech.timoproject.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Post post = Post.builder()
                    .title("[test]아칼리 상향좀...")
                    .content("\n" +
                            "진짜 하 요즘 아칼리로 랭돌리기 너무 힘듬. 아칼리로 게임하면 ᄅᄋ 상대 점수 자판기 되는 느낌ᄏᄏ. 왜 이렇게 힘든지 정리해보겠음.\n" +
                            "메타랑 너무 안 맞음: 요즘 메타가 탱커랑 긴 전투가 대세라서 아칼리 기동성이나 폭딜이 ᄅᄋ 안 먹힘.\n" +
                            "탱커가 너무 단단해서 딜 넣기 힘들고, 스킬 쿨타임 길어서 빈틈 자주 생김.\n" +
                            "난이도 대비 보상 부족함: 아칼리로 플레이하기 진짜 까다롭고, 스킬 맞추기도 어렵고 위치 잘 잡아야 하는데, 보상 너무 적음. 실수 한 번 하면 게임이 흔들리기 일쑤임.\n" +
                            "다른 챔피언들한테 밀림: 요즘 카사딘, 피즈 같은 챔피언들이 너무 강력해서 아칼리가 ᄅᄋ 한없이 약해 보임.\n" +
                            "그런 챔피언들은 안정감도 좋고 딜도 훨씬 강력해서 아칼리랑 비교되면 ᄅᄋ 너무 약해 보임.\n" +
                            "진짜 이 상태로는 아칼리로 게임하기 힘듬. 밸런스 좀 조정해서 아칼리 다시 제대로 플레이할 수 있게 해줬으면 좋겠음. 반박시 니말이 다 맞음." + i)
                    .writer("User["+ (i % 10)+"]")
                    .build();
            System.out.println(postRepository.save(post));
        });
    }
}