package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PageRepository extends JpaRepository<MyPage, Long> {
    @Query("SELECT m FROM MyPage m WHERE m.acceptor.memberId = :acceptorId AND EXISTS (" +
            "SELECT r FROM Review r WHERE r.myPage = m)")
    List<MyPage> findAllReviewedByAcceptorId(@Param("acceptorId") Long acceptorId);


    @Query("SELECT m FROM MyPage m WHERE m.requestor.memberId = :requestorId")
    List<MyPage> findAllByRequestorId(@Param("requestorId") Long requestorId);
}
