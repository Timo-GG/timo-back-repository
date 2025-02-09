DELETE FROM social_account;
DELETE FROM matching_option;
DELETE FROM comment;
DELETE FROM post;
DELETE FROM member;
-- Insert Data
INSERT INTO member (member_id, email, username, role, player_name, player_tag, reg_date, mod_date)
VALUES
    (1, 'test1@example.com', 'hyunuk', 'MEMBER', 'Wooggie', '#KR1', NOW(), NOW()),
    (2, 'test2@example.com', 'chanha', 'MEMBER', '롤찍먹만할게요', '#5103', NOW(), NOW()),
    (3, 'test3@example.com', 'sangwoo', 'ADMIN', '짱아깨비', '#k r', NOW(), NOW()),
    (4, 'test4@example.com', 'pilho', 'MEMBER', '필호우스', '#KR2', NOW(), NOW()),
    (5, 'test5@example.com', 'byeongjun', 'MEMBER', '병나발불어', '#KR1', NOW(), NOW()),
    (6, 'test6@example.com', 'pilho', 'MEMBER', '필호우스', '#KR2', NOW(), NOW()),
    (7, 'test7@example.com', 'kimjiwon', 'MEMBER', '도산안창호', '#3402', NOW(), NOW()),
    (8, 'test8@example.com', 'sangho', 'MEMBER', '백범김구', '#HIGH', NOW(), NOW());