package com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PageRepository extends JpaRepository<MyPage, Long> {
    @Query(value = "SELECT * FROM mypage WHERE acceptor_id = :acceptorId", nativeQuery = true)
    List<MyPage> findAllByAcceptorId(@Param("acceptorId") Long acceptorId);

    @Query(value = "SELECT * FROM mypage WHERE requestor_id = :requestorId", nativeQuery = true)
    List<MyPage> findAllByRequestorId(@Param("requestorId") Long requestorId);
}
