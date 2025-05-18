-- 외래키 체크 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 테이블 비우기
TRUNCATE TABLE member;

-- 모의 멤버 1명 삽입
INSERT INTO member (
    member_id, username, email,
    univ_certified_email, univ_name, department,
    puuid, game_name, tag_line,
    o_auth_provider, role
) VALUES (
             1, 'mockuser', 'mockuser@example.com',
             'mock@seoultech.ac.kr', '서울과학기술대학교', '컴퓨터공학과',
             '-O2mxHCCLutqV-VC6FZzTTDDDF-QlfsGlR9qP7Cwb4E7ujIzdRhrtM5ibhPlXshnx3ehrbxD01crbQ', 'MockSummoner', 'KR1',
             'KAKAO', 'MEMBER'
         );

-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;