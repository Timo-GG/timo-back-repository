package com.tools.seoultech.timoproject.post.domain.entity;

import jakarta.persistence.*;

@Entity
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name", nullable = false)
    private String name;
}
