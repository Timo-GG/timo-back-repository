package com.tools.seoultech.timoproject.post.repository;

<<<<<<< HEAD
import com.tools.seoultech.timoproject.post.domain.Post;
=======
import com.tools.seoultech.timoproject.post.domain.entity.Post;
>>>>>>> #12-crud-repository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, QuerydslPredicateExecutor<Post> {

}
