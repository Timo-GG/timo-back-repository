-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE member_account;


INSERT INTO member_account (member_account_id, username, email, role)
VALUES (1, 'member1', 'Test1@gmail.com', 'MEMBER');

INSERT INTO member_account (member_account_id, username, email, role)
VALUES (2, 'member2', 'Test2@gmail.com', 'ADMIN');

-- 외래키 체크 활성화
SET FOREIGN_KEY_CHECKS = 1;
