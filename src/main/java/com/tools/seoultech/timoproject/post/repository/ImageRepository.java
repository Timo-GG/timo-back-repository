package com.tools.seoultech.timoproject.post.repository;

import com.tools.seoultech.timoproject.post.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
