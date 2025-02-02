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
INSERT INTO user_info (user_info_id, introduce, age, gender, play_position, play_condition, voice_chat, play_style, play_time, game_mode) VALUES
  (1,  'I love competitive play',           'ADULT',  'MALE',    'TOP',           'FIRST',    'ENABLED',    'HARDCORE',     'MORNING',         'NORMAL'),
  (2,  'Casual and fun gamer',               'TEEN',   'FEMALE',  'JUNGLE',        'CONTINUE', 'DISABLED',   'FUN',          'AFTERNOON',       'RANK_DUO'),
  (3,  'Always striving for improvement',    'ADULT',  'MALE',    'MID',           'LAST',     'LISTEN_ONLY','SUB_ACCOUNT',  'NIGHT',           'RANK_FREE'),
  (4,  'Secretive but skilled',              'SECRET', 'SECRET',  'SUPPORT',       'FIRST',    'ENABLED',    'NO_MATTER',    'EARLY_MORNING',   'NO_MATTER'),
  (5,  'Loves team strategy',                'ADULT',  'FEMALE',  'RANGED_DEALER', 'CONTINUE', 'DISABLED',   'HARDCORE',     'MORNING',         'RANK_DUO'),
  (6,  'Passionate about every game',        'TEEN',   'MALE',    'TOP',           'LAST',     'LISTEN_ONLY','FUN',          'NIGHT',           'NORMAL'),
  (7,  'Determined and precise',             'ADULT',  'FEMALE',  'JUNGLE',        'FIRST',    'ENABLED',    'SUB_ACCOUNT',  'AFTERNOON',       'RANK_FREE'),
  (8,  'Enjoys every match',                 'SECRET', 'MALE',    'MID',           'CONTINUE', 'DISABLED',   'NO_MATTER',    'EARLY_MORNING',   'NORMAL'),
  (9,  'Always looking for improvement',     'ADULT',  'FEMALE',  'SUPPORT',       'LAST',     'LISTEN_ONLY','HARDCORE',     'MORNING',         'NO_MATTER'),
  (10, 'Fun-loving and creative',            'TEEN',   'SECRET',  'RANGED_DEALER', 'FIRST',    'ENABLED',    'FUN',          'NIGHT',           'RANK_DUO');

-------------------------------------------------
-- 4. DUO_INFO 테이블
-------------------------------------------------
INSERT INTO duo_info (duo_info_id, duo_play_position, duo_play_time, duo_voice_chat, duo_age) VALUES
  (1,  'MID',           'NIGHT',        'LISTEN_ONLY', 'TEEN'),
  (2,  'SUPPORT',       'MORNING',      'ENABLED',     'ADULT'),
  (3,  'TOP',           'AFTERNOON',    'DISABLED',    'TEEN'),
  (4,  'JUNGLE',        'EARLY_MORNING','LISTEN_ONLY', 'SECRET'),
  (5,  'MID',           'MORNING',      'ENABLED',     'ADULT'),
  (6,  'SUPPORT',       'NIGHT',        'DISABLED',    'TEEN'),
  (7,  'RANGED_DEALER', 'AFTERNOON',    'LISTEN_ONLY', 'ADULT'),
  (8,  'TOP',           'EARLY_MORNING','ENABLED',     'SECRET'),
  (9,  'JUNGLE',        'MORNING',      'DISABLED',    'TEEN'),
  (10, 'MID',           'NIGHT',        'LISTEN_ONLY', 'ADULT');