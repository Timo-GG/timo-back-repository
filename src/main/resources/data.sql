-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 비우기
TRUNCATE TABLE member;

-- 실제 게임 데이터만 사용한 더미 데이터 삽입 (8명)
INSERT INTO member (
    member_id, username, email,
    univ_certified_email, univ_name, department,
    puuid, game_name, tag_line,
    o_auth_provider, role
) VALUES
      (1, 'yorushika_fan', 'yorushika@seoultech.ac.kr',
       'yorushika@seoultech.ac.kr', '서울과학기술대학교', '컴퓨터공학과',
       'EOupVwrHmwpJr0pcUiY_41Y7eoCydoMYhO0E9qL6pusKuI6VcNqGvBrmsU8BSX9qqFg-O9-aaLkZKw', 'YORUSHlKA', 'KR1',
       'KAKAO', 'MEMBER'),

      (2, 'tos_expert', 'tos@yonsei.ac.kr',
       'tos@yonsei.ac.kr', '연세대학교', '전기전자공학부',
       'hJGs7TEKsqGB9BFiKPBGYba6iOsbnxfEMc_7IX2WzpXVhSou5Qp_Ff_EuqT4A8pFt6ZwqeZ3Wtv7SQ', '트리오브세이비어아시는구나', 'TOS',
       'DISCORD', 'MEMBER'),

      (3, 'carrot_meat', 'carrot@snu.ac.kr',
       'carrot@snu.ac.kr', '서울대학교', '컴퓨터공학부',
       'Brmsl8R4XeVwEUEctGqdFs7iV_dHWuJ3r92ei5sRHz8E8dOmTfvrbREnuqPz4QzSPTglhgGU5dnjBg', '고기당근', 'KR1',
       'NAVER', 'MEMBER'),

      (4, 'jjang_gaebi', 'jjang@korea.ac.kr',
       'jjang@korea.ac.kr', '고려대학교', '기계공학과',
       'dC0umu8h3-ZIUMr4WROdbZakaiuoxyf50u1CWuj9fl_yTgFZ0l9IF8BR80YZ5vFtwt2hUxGTfg5IZg', '짱아깨비', 'k r',
       'KAKAO', 'MEMBER'),

      (5, 'wooggie_user', 'wooggie@kaist.ac.kr',
       'wooggie@kaist.ac.kr', '한국과학기술원', '전산학부',
       'epL8VvzuAI6c6bdkqW40n6SFDr5r18649iFp6qgovuhF6ScdNK5bFlUIiiVBAL970nJ-6aoloq_qjw', 'Wooggie', 'KR1',
       'DISCORD', 'MEMBER'),

      (6, 'mingyu_player', 'mingyu@hanyang.ac.kr',
       'mingyu@hanyang.ac.kr', '한양대학교', '산업공학과',
       'UKPOI87zdEu0YHHiMQf58Wf-fDBzsoSV1LR0wgY7UTfk-rQLcocI1AhukqBRbfQHipXahqJzE3vkSA', '민규', 'TAG',
       'NAVER', 'MEMBER'),

      (7, 'yellow_life', 'yellow@inha.ac.kr',
       'yellow@inha.ac.kr', '인하대학교', '정보통신공학과',
       'zBFgoGOI9SUKREbMEs6iNyCyqpujBJwByFZU9sfcYppr1RSN6ZyCVxlxIi8YzE0cIo5t6fm2w0SA8g', '나노란색살래', 'KR1',
       'KAKAO', 'MEMBER'),

      (8, 'smiling_star', 'star@ajou.ac.kr',
       'star@ajou.ac.kr', '아주대학교', '소프트웨어학과',
       'lWBHh0I9apyzMBekPgs0uQzAeoX5AO90slwcXnidBdiUo-zQnhMAw14TKjRYHk01H2iCrOaOnQxvvg', '웃는 별', 'KR1',
       'DISCORD', 'MEMBER');

-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;