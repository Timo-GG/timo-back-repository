package com.tools.seoultech.timoproject.notification;

import java.util.List;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByMemberAccount(Member member);
	List<Notification> findByMemberAccountAndIsReadFalse(Member member);
}
