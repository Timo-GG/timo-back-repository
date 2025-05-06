package com.tools.seoultech.timoproject.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByMemberAccount(MemberAccount memberAccount);
	List<Notification> findByMemberAccountAndIsReadFalse(MemberAccount memberAccount);
}
