DELETE FROM social_account;
DELETE FROM matching_option;
DELETE FROM user_info;
DELETE FROM rating;
DELETE FROM duo_info;

DELETE FROM comment;
DELETE FROM post;
DELETE FROM member;

-------------------------------------------------
-- 1. MEMBER 테이블
-------------------------------------------------

INSERT INTO member (member_id, email, username, role, player_name, player_tag, reg_date, mod_date)
VALUES
(1, 'test1@example.com', 'hyunuk', 'MEMBER', "Wooggie", "#KR1", NOW(), NOW()),
(2, 'test2@example.com', 'chanha', 'MEMBER', "롤찍먹만할게요", "#5103", NOW(), NOW()),
(3, 'test3@example.com', 'sangwoo', 'ADMIN', "짱아깨비", "#k r", NOW(), NOW()),
(4, 'test4@example.com', 'pilho', 'MEMBER', "필호우스", "#KR2", NOW(), NOW()),
(5, 'test5@example.com', 'byeongjun', 'MEMBER', "병나발불어", "#KR1", NOW(), NOW()),
(6, 'test6@example.com', 'pilho', 'MEMBER', "필호우스", "#KR2", NOW(), NOW()),
(7, 'test7@example.com', 'kimjiwon', 'MEMBER', "도산안창호", "#3402", NOW(), NOW()),
(8, 'test8@example.com', 'sangho', 'MEMBER', "백범김구", "#HIGH", NOW(), NOW()),
(9, 'test9@example.com', 'youngjae', 'MEMBER', "행님", "#KR1", NOW(), NOW()),
(10, 'test10@example.com', 'yoonseok', 'MEMBER', "후아유", "#9876", NOW(), NOW());

-------------------------------------------------
-- 2. SOCIAL_ACCOUNT 테이블
-------------------------------------------------
INSERT INTO social_account (social_account_id, member_id, provider, provider_id)
VALUES
(1, 1, 'google', 'sdsdgffgJfgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW90'),
(2, 2, 'facebook', 'qwfd45dsdsgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW8'),
(3, 3, 'naver', 'JfgjWA4OYe42vQbKCnMNxhGATz0hxgsdkisj2340JQfIUWLzaw4'),
(4, 4, 'naver', 'JfgjWA4OYe4ghjkyu12GATz0h34xgsdkisj2340JQfIUWLzaw56'),
(5, 5, 'kakao', 'dffjisxfgjWA4OYe42vQbKCnMNxhGATz0hxg4045JQfIU23WLzaw7'),
(6, 6, 'google', 'sdsdfg4yJfgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW9i'),
(7, 7, 'facebook', 'qwfd45dsdsgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW8g'),
(8, 8, 'naver', 'JfgjWA4OYe42vQbKCnMNxhGATz0hxgsdkisj2340JQfIUWLzawt'),
(9, 9, 'naver', 'JfgjWA4OYe4ghjkyu12GATz0h34xgsdkisj2340JQfIUWLzawq'),
(10, 10, 'kakao', 'dffjisxfgjWA4OYe42vQbKCnMNxhGATz0hxg4045JQfIU23WLzawm');

-------------------------------------------------
-- 3. USER_INFO 테이블
-------------------------------------------------
INSERT INTO user_info (user_info_id, introduce, play_position, play_condition, voice_chat, play_style, game_mode) VALUES
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
-- 4. DUO_INFO 테이블
-------------------------------------------------
INSERT INTO duo_info (duo_info_id, duo_play_position, duo_play_style) VALUES
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
-- 5. Post 테이블
-------------------------------------------------
INSERT INTO post (post_id, title, content, member_id, view_count, like_count, category, mod_date, reg_date)
VALUES
(1, '테스트 제목: 1', '테스트 내용...1', 1, 50, 0, 'NORMAL', now(), now()),
(2, '테스트 제목: 2', '테스트 내용...2', 2, 100, 0, 'NORMAL', now(), now()),
(3, '테스트 제목: 3', '테스트 내용...3', 3, 150, 0, 'NORMAL', now(), now());

-------------------------------------------------
-- 6. Comment 테이블
-------------------------------------------------
INSERT INTO comment (comment_id, member_id, post_id, content, mod_date, reg_date)
VALUES
(1, 5, 1, '테스트 댓글 내용...1', now(), now()),
(2, 4, 2, '테스트 댓글 내용...2', now(), now()),
(3, 3, 2, '테스트 댓글 내용...3', now(), now()),
(4, 2, 3, '테스트 댓글 내용...4', now(), now()),
(5, 1, 3, '테스트 댓글 내용...5', now(), now());

-------------------------------------------------
-- 7. Rating 테이블
-------------------------------------------------
INSERT INTO rating (score, attitude, speech, skill, member_id, duo_id)
VALUES
    (4.5, 'GOOD', 'MANNERS', 'LEARNING', 1, 2),
    (2.5, 'BAD', 'AGGRESSIVE', 'NORMAL', 1, 2);