-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 비우기
TRUNCATE TABLE member;
TRUNCATE TABLE ranking_info;
TRUNCATE TABLE my_page;
TRUNCATE TABLE duo_page;

-- 실제 게임 데이터만 사용한 더미 데이터 삽입 (8명)
INSERT INTO member (
    member_id, username, email,
    univ_certified_email, univ_name, department,
    puuid, game_name, tag_line,
    o_auth_provider, role, term)
VALUES
    (1, 'yorushika_fan', 'louis5103@seoultech.ac.kr',
     'louis5103@seoultech.ac.kr', '서울과학기술대학교', '컴퓨터공학과',
     '8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw', '짱아깨비', 'k r',
     'KAKAO', 'MEMBER', 'NOTHING'),

    (2, 'tos_expert', 'tos@yonsei.ac.kr',
     'tos@yonsei.ac.kr', '연세대학교', '전기전자공학부',
     'nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg', '트리오브세이비어아시는구나', 'TOS',
     'DISCORD', 'MEMBER', 'NOTHING'),

    (3, 'carrot_meat', 'carrot@snu.ac.kr',
     'carrot@snu.ac.kr', '서울대학교', '컴퓨터공학부',
     'H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg', '고기당근', 'KR1',
     'NAVER', 'MEMBER', 'NOTHING'),

    (4, 'jjang_gaebi', 'jjang@korea.ac.kr',
     'jjang@korea.ac.kr', '고려대학교', '기계공학과',
     '_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A', 'YORUSHlKA', 'KR1',
     'KAKAO', 'MEMBER', 'NOTHING'),

    (5, 'wooggie_user', 'wooggie@kaist.ac.kr',
     'wooggie@kaist.ac.kr', '한국과학기술원', '전산학부',
     '2E4RxGivt7BFF_CRAdBpZTarV04psZLpdr5CxVK7Mztv3wyX0CZhTivBwElokYSUDXNzB_5O7dza7A', 'Wooggie', 'KR1',
     'DISCORD', 'MEMBER', 'NOTHING'),

    (6, 'mingyu_player', 'mingyu@hanyang.ac.kr',
     'mingyu@hanyang.ac.kr', '한양대학교', '산업공학과',
     'jSb1d_yjML3gF48MHepEqKDX3zhx5gM67h9zzCwGhrjJ1-_R1636mAmcoMw0gUld_V7nYvjLzSarfA', '민규', 'TAG',
     'NAVER', 'MEMBER', 'NOTHING'),

    (7, 'yellow_life', 'yellow@inha.ac.kr',
     'yellow@inha.ac.kr', '인하대학교', '정보통신공학과',
     'sF_0J4F9qT3dSEtQ3xOqefopRVXJy4vk61J4yGFB7AZhUQMGnC9ob_QFiE38FxOPusWnvZIaW8Yb9g', '나노란색살래', 'KR1',
     'KAKAO', 'MEMBER', 'NOTHING'),

    (8, 'smiling_star', 'star@ajou.ac.kr',
     'star@ajou.ac.kr', '아주대학교', '소프트웨어학과',
     '1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g', '웃는 별', 'KR1',
     'DISCORD', 'MEMBER', 'NOTHING');

INSERT INTO ranking_info (rank_id, member_id, position, mbti, gender, memo)
VALUES
    (1, 1, 'JUNGLE', 'INFP', 'MALE', 'YORUSHIKA 좋아하는 정글러입니다'),
    (2, 2, 'SUPPORT', 'ENFJ', 'MALE', 'TOS 게임도 좋아합니다'),
    (3, 3, 'BOTTOM', 'ISTJ', 'MALE', '고기당근 조합 좋아해요'),
    (4, 4, 'MID', 'ESTP', 'FEMALE', '미드 라이너 짱아깨비'),
    (5, 5, 'SUPPORT', 'INFJ', 'MALE', '서포터로 팀을 도와드려요'),
    (6, 6, 'MID', 'ESFP', 'MALE', '미드 민규입니다'),
    (7, 7, 'BOTTOM', 'ISFP', 'FEMALE', '노란색을 좋아하는 원딜'),
    (8, 8, 'TOP', 'INTJ', 'MALE', '웃는 별처럼 긍정적인 탑라이너');

-- my_page 테이블에 더미 데이터 삽입 (inheritance 전략 고려)
INSERT INTO my_page (
    mypage_id, matching_category, category, status, acceptor_id, requestor_id, review_status, reg_date, mod_date)
VALUES
    (1, 'DUO', 'DUO', 'WAITING', 1, 2, 'UNREVIEWED', now(), now()),
    (2, 'DUO', 'DUO', 'CONNECTED', 3, 4, 'UNREVIEWED', now(), now()),
    (3, 'DUO', 'DUO', 'COMPLETED', 5, 6, 'UNREVIEWED', now(), now()),
    (4, 'DUO', 'DUO', 'DISCONNECTED', 7, 8, 'UNREVIEWED', now(), now()),

    (5, 'SCRIM', 'SCRIM', 'WAITING', 4, 1, 'UNREVIEWED', now(), now()),
    (6, 'SCRIM', 'SCRIM', 'WAITING', 1, 2, 'UNREVIEWED', now(), now()),
    (7, 'SCRIM', 'SCRIM', 'WAITING', 8, 3, 'UNREVIEWED', now(), now());


-- duo_page 테이블에 더미 데이터 삽입 (map_code를 문자열로 수정)
INSERT INTO duo_page (
    mypage_id, map_code,
    acceptor_user_info, requestor_user_info,
    acceptor_member_info, requestor_member_info)
VALUES
    (1, 'RANK',
     '{"myPosition":"JUNGLE","myStyle":"HARDCORE","myStatus":"CONTINUE","myVoice":"ENABLED"}',
     '{"myPosition":"SUPPORT","myStyle":"FUN","myStatus":"FIRST","myVoice":"ENABLED"}',
     '{"riotAccount":{"puuid":"_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A","gameName":"짱아깨비","tagLine":"k r","profileUrl":null},"rankInfo":{"tier":"GOLD","rank":"II","lp":45,"wins":120,"losses":100},"most3Champ":["Lee Sin","Elise","Jarvan IV"]}',
     '{"riotAccount":{"puuid":"nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg","gameName":"트리오브세이비어아시는구나","tagLine":"TOS","profileUrl":null},"rankInfo":{"tier":"PLATINUM","rank":"IV","lp":23,"wins":150,"losses":130},"most3Champ":["Thresh","Lulu","Nami"]}'),

    (2, 'NORMAL',
     '{"myPosition":"BOTTOM","myStyle":"FUN","myStatus":"CONTINUE","myVoice":"ENABLED"}',
     '{"myPosition":"MID","myStyle":"HARDCORE","myStatus":"CONTINUE","myVoice":"DISABLED"}',
     '{"riotAccount":{"puuid":"Brmsl8R4XeVwEUEctGqdFs7iV_dHWuJ3r92ei5sRHz8E8dOmTfvrbREnuqPz4QzSPTglhgGU5dnjBg","gameName":"고기당근","tagLine":"KR1","profileUrl":null},"rankInfo":{"tier":"PLATINUM","rank":"II","lp":67,"wins":200,"losses":180},"most3Champ":["Jinx","Ezreal","Caitlyn"]}',
     '{"riotAccount":{"puuid":"H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg","gameName":"짱아깨비","tagLine":"k r","profileUrl":null},"rankInfo":{"tier":"DIAMOND","rank":"IV","lp":12,"wins":300,"losses":270},"most3Champ":["Ahri","Syndra","LeBlanc"]}'),

    (3, 'HOWLING_ABYSS',
     '{"myPosition":"SUPPORT","myStyle":"FUN","myStatus":"LAST","myVoice":"ENABLED"}',
     '{"myPosition":"MID","myStyle":"FUN","myStatus":"LAST","myVoice":"ENABLED"}',
     '{"riotAccount":{"puuid":"2E4RxGivt7BFF_CRAdBpZTarV04psZLpdr5CxVK7Mztv3wyX0CZhTivBwElokYSUDXNzB_5O7dza7A","gameName":"Wooggie","tagLine":"KR1","profileUrl":null},"rankInfo":{"tier":"GOLD","rank":"I","lp":89,"wins":150,"losses":140},"most3Champ":["Soraka","Janna","Lulu"]}',
     '{"riotAccount":{"puuid":"jSb1d_yjML3gF48MHepEqKDX3zhx5gM67h9zzCwGhrjJ1-_R1636mAmcoMw0gUld_V7nYvjLzSarfA","gameName":"민규","tagLine":"TAG","profileUrl":null},"rankInfo":{"tier":"SILVER","rank":"I","lp":56,"wins":100,"losses":90},"most3Champ":["Zed","Yasuo","Akali"]}'),

    (4, 'RANK',
     '{"myPosition":"BOTTOM","myStyle":"HARDCORE","myStatus":"FIRST","myVoice":"DISABLED"}',
     '{"myPosition":"TOP","myStyle":"HARDCORE","myStatus":"FIRST","myVoice":"DISABLED"}',
     '{"riotAccount":{"puuid":"sF_0J4F9qT3dSEtQ3xOqefopRVXJy4vk61J4yGFB7AZhUQMGnC9ob_QFiE38FxOPusWnvZIaW8Yb9g","gameName":"나노란색살래","tagLine":"KR1","profileUrl":null},"rankInfo":{"tier":"PLATINUM","rank":"III","lp":34,"wins":180,"losses":160},"most3Champ":["Vayne","Kai''Sa","Xayah"]}',
     '{"riotAccount":{"puuid":"1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g","gameName":"웃는 별","tagLine":"KR1","profileUrl":null},"rankInfo":{"tier":"GOLD","rank":"III","lp":78,"wins":130,"losses":120},"most3Champ":["Darius","Garen","Sett"]}');

INSERT INTO scrim_page (mypage_id, head_count, map_code, acceptor_party_info, requestor_party_info, acceptor_member_info, requestor_member_info) VALUES
-- 내전 1: user1팀 vs user4팀 (3vs3)
(5, 3, 'RIFT',
 '[{"discordUsername": "user1#1234", "gameUsername": "GameUser1", "mainPosition": "TOP", "subPosition": "JUNGLE", "championName": "GAREN"}, {"discordUsername": "user2#5678", "gameUsername": "GameUser2", "mainPosition": "JUNGLE", "subPosition": "MID", "championName": "GRAVES"}, {"discordUsername": "user5#7890", "gameUsername": "GameUser5", "mainPosition": "MID", "subPosition": "ADC", "championName": "AZIR"}]',
 '[{"discordUsername": "user4#3456", "gameUsername": "GameUser4", "mainPosition": "TOP", "subPosition": "JUNGLE", "championName": "DARIUS"}, {"discordUsername": "user6#2468", "gameUsername": "GameUser6", "mainPosition": "JUNGLE", "subPosition": "MID", "championName": "LEE_SIN"}, {"discordUsername": "user8#8642", "gameUsername": "GameUser8", "mainPosition": "MID", "subPosition": "ADC", "championName": "YASUO"}]',
 '{"discordUsername": "user1#1234", "gameUsername": "GameUser1", "tier": "GOLD"}',
 '{"discordUsername": "user4#3456", "gameUsername": "GameUser4", "tier": "MASTER"}'
),
-- 내전 2: user2팀 vs user7팀 (3vs3)
(6, 3, 'RIFT',
 '[{"discordUsername": "user2#5678", "gameUsername": "GameUser2", "mainPosition": "ADC", "subPosition": "SUPPORT", "championName": "JINX"}, {"discordUsername": "user3#9012", "gameUsername": "GameUser3", "mainPosition": "SUPPORT", "subPosition": "TOP", "championName": "THRESH"}, {"discordUsername": "user5#7890", "gameUsername": "GameUser5", "mainPosition": "TOP", "subPosition": "JUNGLE", "championName": "FIORA"}]',
 '[{"discordUsername": "user7#1357", "gameUsername": "GameUser7", "mainPosition": "ADC", "subPosition": "MID", "championName": "VAYNE"}, {"discordUsername": "user1#1234", "gameUsername": "GameUser1", "mainPosition": "SUPPORT", "subPosition": "ADC", "championName": "LEONA"}, {"discordUsername": "user6#2468", "gameUsername": "GameUser6", "mainPosition": "MID", "subPosition": "TOP", "championName": "ZED"}]',
 '{"discordUsername": "user2#5678", "gameUsername": "GameUser2", "tier": "PLATINUM"}',
 '{"discordUsername": "user7#1357", "gameUsername": "GameUser7", "tier": "SILVER"}'
),
-- 내전 3: user3팀 vs user8팀 (3vs3)
(7, 3, 'RIFT',
 '[{"discordUsername": "user3#9012", "gameUsername": "GameUser3", "mainPosition": "MID", "subPosition": "TOP", "championName": "AZIR"}, {"discordUsername": "user4#3456", "gameUsername": "GameUser4", "mainPosition": "JUNGLE", "subPosition": "ADC", "championName": "GRAVES"}, {"discordUsername": "user1#1234", "gameUsername": "GameUser1", "mainPosition": "TOP", "subPosition": "JUNGLE", "championName": "GAREN"}]',
 '[{"discordUsername": "user8#8642", "gameUsername": "GameUser8", "mainPosition": "SUPPORT", "subPosition": "MID", "championName": "NAUTILUS"}, {"discordUsername": "user5#7890", "gameUsername": "GameUser5", "mainPosition": "ADC", "subPosition": "SUPPORT", "championName": "JINX"}, {"discordUsername": "user7#1357", "gameUsername": "GameUser7", "mainPosition": "TOP", "subPosition": "JUNGLE", "championName": "DARIUS"}]',
 '{"discordUsername": "user3#9012", "gameUsername": "GameUser3", "tier": "DIAMOND"}',
 '{"discordUsername": "user8#8642", "gameUsername": "GameUser8", "tier": "BRONZE"}'
);


-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;