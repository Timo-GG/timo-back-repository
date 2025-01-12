<<<<<<<< HEAD:src/main/java/com/tools/seoultech/timoproject/post/domain/Post.java
package com.tools.seoultech.timoproject.post.domain;
========
package com.tools.seoultech.timoproject.post.domain.entity;
>>>>>>>> #12-crud-repository:src/main/java/com/tools/seoultech/timoproject/post/domain/entity/Post.java

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length=100, nullable=false)
    private String title;

    @Column(length=1500, nullable=false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
    @JoinColumn(name = "user_account_puuid", nullable = false)
    private UserAccount userAccount;


}
