package com.tools.seoultech.timoproject.notification;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.member.domain.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private String redirectUrl;

	private String message;

	private boolean isRead;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public void markAsRead() {
		this.isRead = true;
	}

}
