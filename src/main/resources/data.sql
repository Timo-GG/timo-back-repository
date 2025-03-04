-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 기존 데이터 삭제 (각 테이블 초기화)
TRUNCATE TABLE rating;
TRUNCATE TABLE comment;
TRUNCATE TABLE post;
TRUNCATE TABLE duo_info;
TRUNCATE TABLE user_info;
TRUNCATE TABLE member;

-------------------------------------------------
-- 1. MEMBER 테이블
-------------------------------------------------

INSERT INTO member (member_id, email, username, nickname, role, player_name, player_tag, reg_date, mod_date)
VALUES
    (1, 'test1@example.com', 'hyunuk', 'hyunuk', 'MEMBER', 'Wooggie', '#KR1', NOW(), NOW()),
    (2, 'test2@example.com', 'chanha', 'chanha', 'MEMBER', '롤찍먹만할게요', '#5103', NOW(), NOW()),
    (3, 'test3@example.com', 'sangwoo', 'sangwoo', 'ADMIN', '짱아깨비', '#k r', NOW(), NOW()),
    (4, 'test4@example.com', 'pilho', 'pilho', 'MEMBER', '필호우스', '#KR2', NOW(), NOW()),
    (5, 'test5@example.com', 'byeongjun', 'byeongjun', 'MEMBER', '병나발불어', '#KR1', NOW(), NOW()),
    (6, 'test6@example.com', 'pilho', 'pilho', 'MEMBER', '필호우스', '#KR2', NOW(), NOW()),
    (7, 'test7@example.com', 'kimjiwon', 'kimjiwon', 'MEMBER', '도산안창호', '#3402', NOW(), NOW()),
    (8, 'test8@example.com', 'sangho', 'sangho', 'MEMBER', '백범김구', '#HIGH', NOW(), NOW()),
    (9, 'test9@example.com', 'youngjae', 'youngjae', 'MEMBER', '행님', '#KR1', NOW(), NOW()),
    (10, 'test10@example.com', 'yoonseok', 'yoonseok', 'MEMBER', '후아유', '#9876', NOW(), NOW());

-------------------------------------------------
-- 2. USER_INFO 테이블
-------------------------------------------------
INSERT INTO user_info (user_info_id, introduce, play_position, play_condition, voice_chat, play_style, game_mode)
VALUES
      (1,  'I love competitive play',            'TOP',           'FIRST',    'ENABLED',    'HARDCORE',     'NORMAL'),
      (2,  'Casual and fun gamer',               'JUNGLE',        'CONTINUE', 'DISABLED',   'FUN',          'RANK_DUO'),
      (3,  'Always striving for improvement',    'MID',           'LAST',     'LISTEN_ONLY','SUB_ACCOUNT',  'RANK_FREE'),
      (4,  'Secretive but skilled',              'SUPPORT',       'FIRST',    'ENABLED',    'NO_MATTER',    'NO_MATTER'),
      (5,  'Loves team strategy',                'RANGED_DEALER', 'CONTINUE', 'DISABLED',   'HARDCORE',     'RANK_DUO'),
      (6,  'Passionate about every game',        'TOP',           'LAST',     'LISTEN_ONLY','FUN',          'NORMAL'),
      (7,  'Determined and precise',             'JUNGLE',        'FIRST',    'ENABLED',    'SUB_ACCOUNT',  'RANK_FREE'),
      (8,  'Enjoys every match',                 'MID',           'CONTINUE', 'DISABLED',   'NO_MATTER',    'NORMAL'),
      (9,  'Always looking for improvement',     'SUPPORT',       'LAST',     'LISTEN_ONLY','HARDCORE',     'NO_MATTER'),
      (10, 'Fun-loving and creative',            'RANGED_DEALER', 'FIRST',    'ENABLED',    'FUN',          'RANK_DUO');

-------------------------------------------------
-- 3. DUO_INFO 테이블
-------------------------------------------------
INSERT INTO duo_info (duo_info_id, duo_play_position, duo_play_style)
VALUES
      (1,  'MID',           'FUN'),
      (2,  'SUPPORT',       'HARDCORE'),
      (3,  'TOP',           'HARDCORE'),
      (4,  'JUNGLE',        'FUN'),
      (5,  'MID',           'HARDCORE'),
      (6,  'SUPPORT',       'FUN'),
      (7,  'RANGED_DEALER', 'NO_MATTER'),
      (8,  'TOP',           'SUB_ACCOUNT'),
      (9,  'JUNGLE',        'NO_MATTER'),
      (10, 'MID',           'HARDCORE');

-------------------------------------------------
-- 4. Post 테이블
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
-- 5. Comment 테이블
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
-- 6. Rating 테이블
-------------------------------------------------
INSERT INTO rating (score, attitude, speech, skill, member_id, duo_id)
VALUES
     (4.5, 'GOOD', 'MANNERS', 'LEARNING', 1, 2),
     (2.5, 'BAD', 'AGGRESSIVE', 'NORMAL', 1, 2);
    
-- 외래키 체크 활성화
SET FOREIGN_KEY_CHECKS = 1;

