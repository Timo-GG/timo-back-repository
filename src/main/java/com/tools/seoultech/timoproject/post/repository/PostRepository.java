package com.tools.seoultech.timoproject.post.repository;

import com.tools.seoultech.timoproject.post.domain.entity.Category;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {
    long countByMemberId(Long memberId);

    List<Post> findByCategory(Category category);
    List<Post> findByTitleContaining(String title);

    @Query("SELECT p FROM Post p WHERE p.memberId = ?1")
    List<Post> findByMemberId(Long memberId);
}
