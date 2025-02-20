package com.tools.seoultech.timoproject.post.domain.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.post.domain.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @Column(name="comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    public void setPost(Post post) {
        if(post != null) {
            this.post = post;
            post.getComments().add(this);
        }
    }
    public void deleteThis(){
        post.getComments().remove(this);
    }

    // TODO: entitymanager. FK 관련 테이블 연관관계 수정.
    public void updateComment(CommentDTO.Request requestDto){
        this.content = requestDto.content();
    }
}
