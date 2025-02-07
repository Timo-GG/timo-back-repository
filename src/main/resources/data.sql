DELETE FROM social_account;
DELETE FROM matching_option;
DELETE FROM comment;
DELETE FROM post;
DELETE FROM member;
-- Insert members
INSERT INTO member (member_id, email, username, role, player_name, player_tag, reg_date, mod_date)
VALUES
(1, 'test1@example.com', 'hyunuk', 'MEMBER', "Wooggie", "#KR1", NOW(), NOW()),
(2, 'test2@example.com', 'chanha', 'MEMBER', "롤찍먹만할게요", "#5103", NOW(), NOW()),
(3, 'test3@example.com', 'sangwoo', 'ADMIN', "짱아깨비", "#k r", NOW(), NOW()),
(4, 'test4@example.com', 'pilho', 'MEMBER', "필호우스", "#KR2", NOW(), NOW()),
(5, 'test5@example.com', 'byeongjun', 'MEMBER', "병나발불어", "#KR1", NOW(), NOW()),
(6, 'test6@example.com', 'pilho', 'MEMBER', "필호우스", "#KR2", NOW(), NOW()),
(7, 'test7@example.com', 'kimjiwon', 'MEMBER', "도산안창호", "#3402", NOW(), NOW()),
(8, 'test8@example.com', 'sangho', 'MEMBER', "백범김구", "#HIGH", NOW(), NOW());

-- Insert Social Accounts
INSERT INTO social_account (social_account_id, member_id, provider, provider_id)
VALUES
(1, 1, 'google', 'sdsdgffgJfgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW'),
(2, 2, 'facebook', 'qwfd45dsdsgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUW'),
(3, 3, 'naver', 'JfgjWA4OYe42vQbKCnMNxhGATz0hxgsdkisj2340JQfIUWLzaw4'),
(4, 4, 'kakao', 'dfsazfgjWA4OYe42vQbKCnMNxhGATz0hxg4045JQfIU23WLzaw4');

-- Insert Matching Options
INSERT INTO matching_option (match_option_id, member_id, introduce, age, gender, voice_chat, play_style, play_time, game_mode)
VALUES
(1, 1, '안녕하세요, 같이 랭크 게임해요!', 'ADULT', 'MALE', 'ENABLED', 'HARDCORE', 'NIGHT', 'RANK'),
(2, 2, '캐주얼하게 게임 즐겨요!', 'ADULT', 'FEMALE', 'DISABLED', 'FUN', 'AFTERNOON', 'NORMAL');

-- Insert post
INSERT INTO post (post_id, title, content, member_id, view_count, category, mod_date, reg_date)
VALUES
(1, '테스트 제목: 1', '테스트 내용...1', 1, 50, 'NORMAL', now(), now()),
(2, '테스트 제목: 2', '테스트 내용...2', 2, 100, 'NORMAL', now(), now()),
(3, '테스트 제목: 3', '테스트 내용...3', 3, 150, 'NORMAL', now(), now());
-- Insert comment
INSERT INTO comment (comment_id, member_id, post_id, content)
VALUES
(1, 5, 1, '테스트 댓글 내용...1'),
(2, 4, 2, '테스트 댓글 내용...2'),
(3, 3, 2, '테스트 댓글 내용...3'),
(4, 2, 3, '테스트 댓글 내용...4'),
(5, 1, 3, '테스트 댓글 내용...5');
-- Insert Tag