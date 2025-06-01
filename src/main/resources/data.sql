-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 비우기
TRUNCATE TABLE member;
TRUNCATE TABLE ranking_info;
TRUNCATE TABLE my_page;
TRUNCATE TABLE duo_page;
TRUNCATE TABLE scrim_page;

-- 기존 Member 테이블 데이터 유지 (변경사항 없음)
INSERT INTO member (
    member_id, username, email,
    univ_certified_email, univ_name, department,
    puuid, game_name, tag_line,
    o_auth_provider, role)
VALUES
    (1, 'yorushika_fan', 'yorushika@seoultech.ac.kr',
     'yorushika@seoultech.ac.kr', '서울과학기술대학교', '컴퓨터공학과',
     '8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw', '짱아깨비', 'k r',
     'KAKAO', 'MEMBER'),

    (2, 'tos_expert', 'tos@yonsei.ac.kr',
     'tos@yonsei.ac.kr', '연세대학교', '전기전자공학부',
     'nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg', '트리오브세이비어아시는구나', 'TOS',
     'DISCORD', 'MEMBER'),

    (3, 'carrot_meat', 'carrot@snu.ac.kr',
     'carrot@snu.ac.kr', '서울대학교', '컴퓨터공학부',
     'H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg', '고기당근', 'KR1',
     'NAVER', 'MEMBER'),

    (4, 'jjang_gaebi', 'jjang@korea.ac.kr',
     'jjang@korea.ac.kr', '고려대학교', '기계공학과',
     '_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A', 'YORUSHlKA', 'KR1',
     'KAKAO', 'MEMBER'),

    (5, 'wooggie_user', 'wooggie@kaist.ac.kr',
     'wooggie@kaist.ac.kr', '한국과학기술원', '전산학부',
     '2E4RxGivt7BFF_CRAdBpZTarV04psZLpdr5CxVK7Mztv3wyX0CZhTivBwElokYSUDXNzB_5O7dza7A', 'Wooggie', 'KR1',
     'DISCORD', 'MEMBER'),

    (6, 'mingyu_player', 'mingyu@hanyang.ac.kr',
     'mingyu@hanyang.ac.kr', '한양대학교', '산업공학과',
     'jSb1d_yjML3gF48MHepEqKDX3zhx5gM67h9zzCwGhrjJ1-_R1636mAmcoMw0gUld_V7nYvjLzSarfA', '민규', 'TAG',
     'NAVER', 'MEMBER'),

    (7, 'yellow_life', 'yellow@inha.ac.kr',
     'yellow@inha.ac.kr', '인하대학교', '정보통신공학과',
     'sF_0J4F9qT3dSEtQ3xOqefopRVXJy4vk61J4yGFB7AZhUQMGnC9ob_QFiE38FxOPusWnvZIaW8Yb9g', '나노란색살래', 'KR1',
     'KAKAO', 'MEMBER'),

    (8, 'smiling_star', 'star@ajou.ac.kr',
     'star@ajou.ac.kr', '아주대학교', '소프트웨어학과',
     '1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g', '웃는 별', 'KR1',
     'DISCORD', 'MEMBER');

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

-- my_page 테이블 데이터
INSERT INTO my_page (
    mypage_id, matching_category, category, status, acceptor_id, requestor_id)
VALUES
    (1, 'DUO', 'DUO', 'WAITING', 1, 2),
    (2, 'DUO', 'DUO','CONNECTED', 3, 4),
    (3, 'DUO', 'DUO', 'COMPLETED', 5, 6),
    (4, 'DUO', 'DUO', 'DISCONNECTED', 7, 8),

    (5, 'SCRIM', 'SCRIM', 'WAITING', 4, 1),
    (6, 'SCRIM', 'SCRIM', 'WAITING', 1, 2),
    (7, 'SCRIM', 'SCRIM', 'WAITING', 8, 3);

INSERT INTO duo_page (
    mypage_id, map_code,
    acceptor_user_info, requestor_user_info,
    acceptor_member_info, requestor_member_info)
VALUES
    (1, 'RANK',
     '{"myVoice":"DISABLED","myStyle":"HARDCORE","myStatus":"LAST"}',
     '{"myVoice":"ENABLED","myStyle":"FUN","myStatus":"FIRST"}',
     '{"puuid":"8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw","gameName":"짱아깨비","tagLine":"k r","profileUrl":null,"tier":"GOLD","rank":"II","most3Champ":["Lee Sin","Elise","Jarvan IV"],"myPosition":"JUNGLE","univName":"서울과학기술대학교","department":"컴퓨터공학과","gender":"MALE","mbti":"INFP"}',
     '{"puuid":"nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg","gameName":"트리오브세이비어아시는구나","tagLine":"TOS","profileUrl":null,"tier":"PLATINUM","rank":"IV","most3Champ":["Thresh","Lulu","Nami"],"myPosition":"SUPPORT","univName":"연세대학교","department":"전기전자공학부","gender":"MALE","mbti":"ENFJ"}'),

    (2, 'NORMAL',
     '{"myVoice":"ENABLED","myStyle":"FUN","myStatus":"CONTINUE"}',
     '{"myVoice":"DISABLED","myStyle":"HARDCORE","myStatus":"FIRST"}',
     '{"puuid":"H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg","gameName":"고기당근","tagLine":"KR1","profileUrl":null,"tier":"PLATINUM","rank":"II","most3Champ":["Jinx","Ezreal","Caitlyn"],"myPosition":"BOTTOM","univName":"서울대학교","department":"컴퓨터공학부","gender":"MALE","mbti":"ISTJ"}',
     '{"puuid":"_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A","gameName":"YORUSHlKA","tagLine":"KR1","profileUrl":null,"tier":"DIAMOND","rank":"IV","most3Champ":["Ahri","Syndra","LeBlanc"],"myPosition":"MID","univName":"고려대학교","department":"기계공학과","gender":"FEMALE","mbti":"ESTP"}'),

    (3, 'HOWLING_ABYSS',
     '{"myVoice":"ENABLED","myStyle":"FUN","myStatus":"CONTINUE"}',
     '{"myVoice":"DISABLED","myStyle":"FUN","myStatus":"FIRST"}',
     '{"puuid":"2E4RxGivt7BFF_CRAdBpZTarV04psZLpdr5CxVK7Mztv3wyX0CZhTivBwElokYSUDXNzB_5O7dza7A","gameName":"Wooggie","tagLine":"KR1","profileUrl":null,"tier":"GOLD","rank":"I","most3Champ":["Soraka","Janna","Lulu"],"myPosition":"SUPPORT","univName":"한국과학기술원","department":"전산학부","gender":"MALE","mbti":"INFJ"}',
     '{"puuid":"jSb1d_yjML3gF48MHepEqKDX3zhx5gM67h9zzCwGhrjJ1-_R1636mAmcoMw0gUld_V7nYvjLzSarfA","gameName":"민규","tagLine":"TAG","profileUrl":null,"tier":"SILVER","rank":"I","most3Champ":["Zed","Yasuo","Akali"],"myPosition":"MID","univName":"한양대학교","department":"산업공학과","gender":"MALE","mbti":"ESFP"}'),

    (4, 'RANK',
     '{"myVoice":"DISABLED","myStyle":"HARDCORE","myStatus":"LAST"}',
     '{"myVoice":"DISABLED","myStyle":"HARDCORE","myStatus":"CONTINUE"}',
     '{"puuid":"sF_0J4F9qT3dSEtQ3xOqefopRVXJy4vk61J4yGFB7AZhUQMGnC9ob_QFiE38FxOPusWnvZIaW8Yb9g","gameName":"나노란색살래","tagLine":"KR1","profileUrl":null,"tier":"PLATINUM","rank":"III","most3Champ":["Vayne","Kai''Sa","Xayah"],"myPosition":"BOTTOM","univName":"인하대학교","department":"정보통신공학과","gender":"FEMALE","mbti":"ISFP"}',
     '{"puuid":"1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g","gameName":"웃는 별","tagLine":"KR1","profileUrl":null,"tier":"GOLD","rank":"III","most3Champ":["Darius","Garen","Sett"],"myPosition":"TOP","univName":"아주대학교","department":"소프트웨어학과","gender":"MALE","mbti":"INTJ"}');

-- scrim_page 테이블 데이터
INSERT INTO scrim_page (mypage_id, head_count, map_code, acceptor_party_info, requestor_party_info, acceptor_member_info, requestor_member_info) VALUES
    (5, 3, 'RIFT',
     '[{"puuid":"_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A","gameName":"YORUSHlKA","tagLine":"KR1","profileUrl":null,"tier":"DIAMOND","rank":"IV","most3Champ":["Ahri","Syndra","LeBlanc"],"myPosition":"MID"}]',
     '[{"puuid":"8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw","gameName":"짱아깨비","tagLine":"k r","profileUrl":null,"tier":"GOLD","rank":"II","most3Champ":["Lee Sin","Elise","Jarvan IV"],"myPosition":"JUNGLE"}]',
     '{"puuid":"_GcmJPP4mRKxDSka41Glg6cOJ6YEMP2TdJrb2Eq8QqvM4maTmz8fUyaVFOAchm2vOOXppAsrWxJ33A","gameName":"YORUSHlKA","tagLine":"KR1","profileUrl":null,"tier":"DIAMOND","rank":"IV","most3Champ":["Ahri","Syndra","LeBlanc"],"myPosition":"MID","univName":"고려대학교","department":"기계공학과","gender":"FEMALE","mbti":"ESTP"}',
     '{"puuid":"8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw","gameName":"짱아깨비","tagLine":"k r","profileUrl":null,"tier":"GOLD","rank":"II","most3Champ":["Lee Sin","Elise","Jarvan IV"],"myPosition":"JUNGLE","univName":"서울과학기술대학교","department":"컴퓨터공학과","gender":"MALE","mbti":"INFP"}'
    ),
    (6, 3, 'RIFT',
     '[{"puuid":"8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw","gameName":"짱아깨비","tagLine":"k r","profileUrl":null,"tier":"GOLD","rank":"II","most3Champ":["Lee Sin","Elise","Jarvan IV"],"myPosition":"JUNGLE"}]',
     '[{"puuid":"nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg","gameName":"트리오브세이비어아시는구나","tagLine":"TOS","profileUrl":null,"tier":"PLATINUM","rank":"IV","most3Champ":["Thresh","Lulu","Nami"],"myPosition":"SUPPORT"}]',
     '{"puuid":"8EGZHOO06vP0fft1Yp4ECBml3x9_fkI7V6veALWs5GTeAdyNTbNSbtaqo-HsCSTvWjhkWfaVBhcBcw","gameName":"짱아깨비","tagLine":"k r","profileUrl":null,"tier":"GOLD","rank":"II","most3Champ":["Lee Sin","Elise","Jarvan IV"],"myPosition":"JUNGLE","univName":"서울과학기술대학교","department":"컴퓨터공학과","gender":"MALE","mbti":"INFP"}',
     '{"puuid":"nVCMArF5NcP3QNpJHul7WdSz94t5XsC5B834p0U5sCmm-hrPVcwapto6Fe_tClDGcoI6FrhA9Cz6sg","gameName":"트리오브세이비어아시는구나","tagLine":"TOS","profileUrl":null,"tier":"PLATINUM","rank":"IV","most3Champ":["Thresh","Lulu","Nami"],"myPosition":"SUPPORT","univName":"연세대학교","department":"전기전자공학부","gender":"MALE","mbti":"ENFJ"}'
    ),
    (7, 3, 'ABYSS',
     '[{"puuid":"1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g","gameName":"웃는 별","tagLine":"KR1","profileUrl":null,"tier":"GOLD","rank":"III","most3Champ":["Darius","Garen","Sett"],"myPosition":"TOP"}]',
     '[{"puuid":"H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg","gameName":"고기당근","tagLine":"KR1","profileUrl":null,"tier":"PLATINUM","rank":"II","most3Champ":["Jinx","Ezreal","Caitlyn"],"myPosition":"BOTTOM"}]',
     '{"puuid":"1UizYcdkcZPAyuoG45kn8x_VXK53-xWDW6sxbxqgghdbzNdIwJNflZFHPiEq3PcXemdAnfFrIIGM6g","gameName":"웃는 별","tagLine":"KR1","profileUrl":null,"tier":"GOLD","rank":"III","most3Champ":["Darius","Garen","Sett"],"myPosition":"TOP","univName":"아주대학교","department":"소프트웨어학과","gender":"MALE","mbti":"INTJ"}',
     '{"puuid":"H2qph9yZZEzSU7yP83gltR7naLJla-wS2ZCCElDWmqVKfNHx804xxh2uyMtvXB6Q5uL9KRP1-ZQLPg","gameName":"고기당근","tagLine":"KR1","profileUrl":null,"tier":"PLATINUM","rank":"II","most3Champ":["Jinx","Ezreal","Caitlyn"],"myPosition":"BOTTOM","univName":"서울대학교","department":"컴퓨터공학부","gender":"MALE","mbti":"ISTJ"}'
    );

-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;
