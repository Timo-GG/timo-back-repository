CREATE TABLE fcm_tokens (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            member_id BIGINT NOT NULL,
                            token VARCHAR(500) NOT NULL,
                            device_type VARCHAR(20) DEFAULT 'WEB',
                            is_active BOOLEAN DEFAULT TRUE,
                            INDEX idx_member_id (member_id),
                            INDEX idx_token (token),
                            INDEX idx_active_tokens (member_id, is_active)
);