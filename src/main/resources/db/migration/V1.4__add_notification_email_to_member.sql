-- V1.4__add_notification_email_to_member.sql
ALTER TABLE member
    ADD COLUMN notification_email VARCHAR(255) NULL;

-- 인덱스 추가 (이메일 알림 발송 시 조회 성능 향상)
CREATE INDEX idx_member_notification_email ON member(notification_email);