package com.tools.seoultech.timoproject.post.domain.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Deprecated
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount extends BaseEntity {
    @Id
    private String puuid;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private String tagLine;
}
