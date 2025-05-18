package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PageRepository extends JpaRepository<MyPage, Long> {
}
