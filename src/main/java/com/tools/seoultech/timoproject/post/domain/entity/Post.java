package com.tools.seoultech.timoproject.post.domain.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "post")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    // 게시글 고유 아이디
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // 게시글 정보: 제목, 작성자, 내용, 이미지
    @Column(length=100, nullable=false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length=1500, nullable=false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    // 게시글 추가 정보: 카테고리, 조회 수, 좋아요 수
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private Category category = Category.NORMAL;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0;

    // 게시글 연관관계: 댓글, 좋아요
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    public void updatePost(Long id, PostDTO.Request request) {
        if(id != null && this.id.equals(id)){
            this.title = request.title();
            this.content = request.content();
        }
    }
    public void increaseViewCount() { this.viewCount++; }
    public void increaseLikeCount() { this.likeCount++; }
    public void decreaseLikeCount() { this.likeCount--; }
}
