-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 데이터 삭제 (각 테이블 초기화)
TRUNCATE TABLE rating;
TRUNCATE TABLE comment;
TRUNCATE TABLE post;
TRUNCATE TABLE member;

-------------------------------------------------
-- 1. MEMBER 테이블
-------------------------------------------------
INSERT INTO member (member_id, email, nickname, role, player_name, player_tag, profile_image_id, reg_date, mod_date)
VALUES
    (1,  'test1@example.com',  'hyunuk',    'MEMBER', 'Wooggie',       '#KR1',  1, NOW(), NOW()),
    (2,  'test2@example.com',  'chanha',    'MEMBER', '롤찍먹만할게요', '#5103', 2, NOW(), NOW()),
    (3,  'test3@example.com',  'sangwoo',   'ADMIN',  '짱아깨비',      '#k r',  1, NOW(), NOW()),
    (4,  'test4@example.com',  'pilho',     'MEMBER', '필호우스',      '#KR2',  2, NOW(), NOW()),
    (5,  'test5@example.com',  'byeongjun', 'MEMBER', '병나발불어',    '#KR1',  1, NOW(), NOW()),
    (6,  'test6@example.com',  'pilho',     'MEMBER', '필호우스',      '#KR2',  2, NOW(), NOW()),
    (7,  'test7@example.com',  'kimjiwon',  'MEMBER', '도산안창호',     '#3402', 1, NOW(), NOW()),
    (8,  'test8@example.com',  'sangho',    'MEMBER', '백범김구',      '#HIGH', 2, NOW(), NOW()),
    (9,  'test9@example.com',  'youngjae',  'MEMBER', '행님',          '#KR1',  1, NOW(), NOW()),
    (10, 'test10@example.com', 'yoonseok',  'MEMBER', '후아유',        '#9876', 2, NOW(), NOW());

-------------------------------------------------
-- 2. Post 테이블
-------------------------------------------------
INSERT INTO post (post_id, title, content, member_id, view_count, like_count, category, reg_date, mod_date)
VALUES
    (1, '테스트 제목: 1', '테스트 내용...1', 1, 50, 0, 'NORMAL', now(), now() + INTERVAL 1 SECOND),
    (2, '테스트 제목: 2', '테스트 내용...2', 2, 100, 0, 'NORMAL', now(), now()),
    (3, '테스트 제목: 3', '테스트 내용...3', 3, 150, 0, 'NORMAL', now(), now()),
    (4, '테스트 제목: 11', '테스트 내용...11', 1, 150, 0, 'NORMAL', now() - INTERVAL 1 DAY, now() + INTERVAL 1 SECOND),
    (5, '테스트 제목: 22', '테스트 내용...22', 2, 100, 0, 'CREATIVITY', now() - INTERVAL 1 DAY, now()),
    (6, '테스트 제목: 33', '테스트 내용...33', 3, 50, 0, 'CREATIVITY', now() - INTERVAL 3 DAY, now() - INTERVAL 1 SECOND);

-------------------------------------------------
-- 3. Comment 테이블
-------------------------------------------------
INSERT INTO comment (comment_id, member_id, post_id, content, reg_date, mod_date)
VALUES
    (1, 5, 1, '테스트 댓글 내용...1', now() + INTERVAL 8 DAY, now() + INTERVAL 2 DAY),
    (2, 4, 2, '테스트 댓글 내용...2', now() + INTERVAL 7 DAY, now() + INTERVAL 1 DAY),
    (3, 3, 2, '테스트 댓글 내용...3', now() + INTERVAL 6 DAY, now()),

    (4, 2, 3, '테스트 댓글 내용...4', now() + INTERVAL 5 DAY, now()),
    (5, 1, 3, '테스트 댓글 내용...5', now() + INTERVAL 4 DAY, now() + INTERVAL 5 DAY),

    (6, 2, 3, '테스트 댓글 내용...4', now() + INTERVAL 3 DAY, now()),
    (7, 2, 3, '테스트 댓글 내용...5', now() + INTERVAL 2 DAY, now()),

    (8, 1, 1, '테스트 댓글 내용...4', now() + INTERVAL 1 DAY, now()),
    (9, 1, 1, '테스트 댓글 내용...5', now() - INTERVAL 1 DAY, now() - INTERVAL 1 DAY);

-------------------------------------------------
-- 4. PostLike 테이블
-------------------------------------------------
INSERT INTO image(image_id, post_id, base64)
    VALUES
        (1, 1, "image_1"),
        (2, 1, "image_2"),
        (3, 1, "image_3");

-------------------------------------------------
-- 5. Rating 테이블
-------------------------------------------------
-- INSERT INTO rating (score, attitude, speech, skill, member_id, duo_id)
-- VALUES
--     (4.5, 'GOOD', 'MANNERS', 'LEARNING', 1, 2),
--     (2.5, 'BAD', 'AGGRESSIVE', 'NORMAL', 1, 2);

INSERT INTO member_account (member_account_id, username, email, role)
VALUES (1, 'user1', 'test1@gmail.com', 'MEMBER');

INSERT INTO member_account (member_account_id, username, email, role)
VALUES (2, 'user2', 'test2@gmail.com', 'ADMIN');
-------------------------------------------------
-- 5. MatchingOption  테이블
-------------------------------------------------
INSERT INTO user_info (user_info_id, introduce, game_mode, play_position, play_condition, voice_chat, play_style)
VALUES
    (1, '팀워크 중요111', 'RANK', 'RANGED_DEALER', 'FIRST', 'ENABLED', 'FUN'),
    (2, '팀워크 중요222', 'RANK', 'SUPPORT', 'FIRST', 'ENABLED', 'FUN'),
    (3, '팀워크 중요333', 'RANK', 'TOP', 'FIRST', 'ENABLED', 'FUN'),
    (4, '팀워크 중요444', 'RANK', 'MID', 'FIRST', 'ENABLED', 'FUN'),
    (5, '팀워크 중요555', 'RANK', 'SUPPORT', 'FIRST', 'ENABLED', 'HARDCORE'),
    (6, '팀워크 중요666', 'RANK', 'SUPPORT', 'FIRST', 'DISABLED', 'HARDCORE'),
    (7, '팀워크 중요777', 'RANK', 'SUPPORT', 'FIRST', 'ENABLED', 'HARDCORE'),
    (8, '팀워크 중요888', 'RANK', 'SUPPORT', 'FIRST', 'ENABLED', 'HARDCORE');

INSERT INTO duo_info (duo_info_id, duo_play_position, duo_play_style)
VALUES
    (1, 'SUPPORT', 'FUN'),
    (2, 'RANGED_DEALER', 'FUN'),
    (3, 'RANGED_DEALER', 'FUN'),
    (4, 'JUNGLE', 'FUN'),
    (5, 'RANGED_DEALER', 'FUN'),
    (6, 'JUNGLE', 'FUN'),
    (7, 'MID', 'FUN'),
    (8, 'TOP', 'FUN');

UPDATE member
SET user_info_id = 1, duo_info_id = 1
WHERE member_id = 1;

UPDATE member
SET user_info_id = 2, duo_info_id = 2
WHERE member_id = 2;

UPDATE member
SET user_info_id = 3, duo_info_id = 3
WHERE member_id = 3;

UPDATE member
SET user_info_id = 4, duo_info_id = 4
WHERE member_id = 4;

UPDATE member
SET user_info_id = 5, duo_info_id = 5
WHERE member_id = 5;

UPDATE member
SET user_info_id = 6, duo_info_id = 6
WHERE member_id = 6;

UPDATE member
SET user_info_id = 7, duo_info_id = 7
WHERE member_id = 7;

UPDATE member
SET user_info_id = 8, duo_info_id = 8
WHERE member_id = 8;




-- 외래키 체크 활성화
SET FOREIGN_KEY_CHECKS = 1;
