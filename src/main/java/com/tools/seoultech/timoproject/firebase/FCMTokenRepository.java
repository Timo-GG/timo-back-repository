package com.tools.seoultech.timoproject.firebase;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {
    List<FCMToken> findByMemberIdInAndIsActiveTrue(List<Long> memberIds);
    Optional<FCMToken> findByMemberIdAndToken(Long memberId, String token);

    @Modifying
    @Query("UPDATE FCMToken f SET f.isActive = false WHERE f.memberId = :memberId AND f.token = :token")
    void deactivateToken(Long memberId, String token);

    @Modifying
    @Query("UPDATE FCMToken f SET f.isActive = false WHERE f.token IN :tokens")
    void deactivateTokensByTokenValues(@Param("tokens") List<String> tokens);
}
