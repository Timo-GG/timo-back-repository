package com.tools.seoultech.timoproject.post.repository;

import com.tools.seoultech.timoproject.post.domain.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 좋아요 개수 카운트.
    @Query("SELECT COUNT(l) FROM PostLike l WHERE l.post.id = :postId")
    Integer countByPostId(@Param("postId") Long postId);

    // 해당 게시글에서 동일 사용자의 중복 좋아요 체크.
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM PostLike l WHERE l.member.id = :memberId AND l.post.id = :postId")
    Boolean existsByMemberId(@Param("memberId") Long memberId, @Param("postId") Long postId);

    void deleteByMemberId(Long memberId);
}
