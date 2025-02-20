package com.tools.seoultech.timoproject.post.domain.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
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
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length=100, nullable=false)
    private String title;

    @Column(length=1500, nullable=false)
    private String content;

    private Long memberId;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private Category category = Category.NORMAL;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "post")
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
