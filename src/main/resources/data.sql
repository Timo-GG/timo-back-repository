-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 데이터 삭제 (각 테이블 초기화)
TRUNCATE TABLE rating;
TRUNCATE TABLE comment;
TRUNCATE TABLE post;
TRUNCATE TABLE member;
-- user_info, duo_info 테이블 관련 삭제

-------------------------------------------------
-- 1. MEMBER 테이블
-------------------------------------------------
INSERT INTO member (member_id, email, nickname, role, player_name, player_tag, reg_date, mod_date)
VALUES
    (1,  'test1@example.com',  'hyunuk',    'MEMBER', 'Wooggie',       '#KR1',  NOW(), NOW()),
    (2,  'test2@example.com',  'chanha',    'MEMBER', '롤찍먹만할게요', '#5103', NOW(), NOW()),
    (3,  'test3@example.com',  'sangwoo',   'ADMIN',  '짱아깨비',      '#k r',  NOW(), NOW()),
    (4,  'test4@example.com',  'pilho',     'MEMBER', '필호우스',      '#KR2',  NOW(), NOW()),
    (5,  'test5@example.com',  'byeongjun', 'MEMBER', '병나발불어',    '#KR1',  NOW(), NOW()),
    (6,  'test6@example.com',  'pilho',     'MEMBER', '필호우스',      '#KR2',  NOW(), NOW()),
    (7,  'test7@example.com',  'kimjiwon',  'MEMBER', '도산안창호',     '#3402', NOW(), NOW()),
    (8,  'test8@example.com',  'sangho',    'MEMBER', '백범김구',      '#HIGH', NOW(), NOW()),
    (9,  'test9@example.com',  'youngjae',  'MEMBER', '행님',          '#KR1',  NOW(), NOW()),
    (10, 'test10@example.com', 'yoonseok',  'MEMBER', '후아유',        '#9876', NOW(), NOW());

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
-- 4. Rating 테이블
-------------------------------------------------
INSERT INTO rating (score, attitude, speech, skill, member_id, duo_id)
VALUES
    (4.5, 'GOOD', 'MANNERS', 'LEARNING', 1, 2),
    (2.5, 'BAD', 'AGGRESSIVE', 'NORMAL', 1, 2);

-- 외래키 체크 활성화
SET FOREIGN_KEY_CHECKS = 1;
