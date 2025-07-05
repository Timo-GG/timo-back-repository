package com.tools.seoultech.timoproject.auth.univ.service;

import com.tools.seoultech.timoproject.auth.univ.entity.University;
import com.tools.seoultech.timoproject.auth.univ.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversitySetupService {

    private final UniversityRepository universityRepository;

    @Transactional
    public void initializeUniversities() {
        // 테이블에 데이터가 이미 있으면 중복 저장을 방지하기 위해 실행하지 않음
        if (universityRepository.count() > 0) {
            log.info("University table is not empty. Initialization skipped.");
            return;
        }

        List<University> universities = List.of(
                new University("가천대학교", "gachon.ac.kr"),
                new University("강원대학교", "kangwon.ac.kr"),
                new University("건국대학교", "konkuk.ac.kr"),
                new University("건국대학교(글로컬)", "kku.ac.kr"),
                new University("경기과학기술대학교", "gtec.ac.kr"),
                new University("경기대학교", "kyonggi.ac.kr"),
                new University("경북대학교", "knu.ac.kr"),
                new University("경인교육대학교", "ginue.ac.kr"),
                new University("경희대학교", "khu.ac.kr"),
                new University("계원예술대학교", "kaywon.ac.kr"),
                new University("고려대학교", "korea.ac.kr"),
                new University("광운대학교", "kw.ac.kr"),
                new University("국민대학교", "kookmin.ac.kr"),
                new University("단국대학교", "dankook.ac.kr"),
                new University("덕성여자대학교", "duksung.ac.kr"),
                new University("동국대학교", "dongguk.edu"),
                new University("동국대학교(경주)", "dongguk.ac.kr"),
                new University("동덕여자대학교", "dongduk.ac.kr"),
                new University("명지대학교", "mju.ac.kr"),
                new University("명지전문대학교", "mjc.ac.kr"),
                new University("서강대학교", "sogang.ac.kr"),
                new University("서경대학교", "skuniv.ac.kr"),
                new University("서울과학기술대학교", "seoultech.ac.kr"),
                new University("서울교육대학교", "snue.ac.kr"),
                new University("서울대학교", "snu.ac.kr"),
                new University("서울시립대학교", "uos.ac.kr"),
                new University("서울여자대학교", "swu.ac.kr"),
                new University("성균관대학교", "skku.edu"),
                new University("성신여자대학교", "sungshin.ac.kr"),
                new University("세종대학교", "sju.ac.kr"),
                new University("숙명여자대학교", "sookmyung.ac.kr"),
                new University("숭실대학교", "soongsil.ac.kr"),
                new University("아주대학교", "ajou.ac.kr"),
                new University("연세대학교", "yonsei.ac.kr"),
                new University("영남대학교", "ynu.ac.kr"),
                new University("이화여자대학교", "ewhain.net"),
                new University("인천대학교", "inu.ac.kr"),
                new University("인하공업전문대학", "itc.ac.kr"),
                new University("인하대학교", "inha.ac.kr"),
                new University("전남대학교", "jnu.ac.kr"),
                new University("전북대학교", "jbnu.ac.kr"),
                new University("중앙대학교", "cau.ac.kr"),
                new University("충북대학교", "chungbuk.ac.kr"),
                new University("한국방송통신대학교", "knou.ac.kr"),
                new University("한국공학대학교", "tukorea.ac.kr"),
                new University("한국예술종합학교", "karts.ac.kr"),
                new University("한국외국어대학교", "hufs.ac.kr"),
                new University("한국체육대학교", "knsu.ac.kr"),
                new University("한양대학교", "hanyang.ac.kr"),
                new University("한양대학교(ERICA)", "hanyang.ac.kr"),
                new University("홍익대학교", "hongik.ac.kr"),
                new University("대구경북과학기술원", "dgist.ac.kr"),
                new University("광주과학기술원", "gist.ac.kr"),
                new University("한국과학기술원", "kaist.ac.kr"),
                new University("포항공과대학교", "postech.ac.kr"),
                new University("울산과학기술원", "unist.ac.kr"),
                new University("계명대학교", "kmu.ac.kr"),
                new University("조선대학교", "chosun.kr"),
                new University("경상국립대학교", "gnu.ac.kr"),
                new University("동아대학교", "donga.ac.kr"),
                new University("대구대학교", "daegu.ac.kr"),
                new University("동의대학교", "deu.ac.kr"),
                new University("충남대학교", "cnu.ac.kr"),
                new University("부경대학교", "pknu.ac.kr"),
                new University("서울사이버대학교", "iscu.ac.kr"),
                new University("한양사이버대학교", "hycu.ac.kr"),
                new University("원광대학교", "wku.ac.kr"),
                new University("경희사이버대학교", "khcu.ac.kr"),
                new University("서울디지털대학교", "sdu.ac.kr"),
                new University("백석대학교", "bu.ac.kr"),
                new University("부천대학교", "bc.ac.kr"),
                new University("대구가톨릭대학교", "cu.ac.kr"),
                new University("한양여자대학교", "hywoman.ac.kr"),
                new University("호서대학교", "hoseo.edu"),
                new University("영진전문대학교", "yjc.ac.kr"),
                new University("공주대학교", "kongju.ac.kr"),
                new University("경성대학교", "ks.ac.kr"),
                new University("신구대학교", "shingu.ac.kr"),
                new University("한남대학교", "hannam.ac.kr"),
                new University("울산대학교", "ulsan.ac.kr"),
                new University("대림대학교", "daelim.ac.kr"),
                new University("동서울대학교", "dsc.ac.kr"),
                new University("청주대학교", "cju.ac.kr"),
                new University("경남정보대학교", "kit.ac.kr"),
                new University("동양미래대학교", "dongyang.ac.kr"),
                new University("대구보건대학교", "dhc.ac.kr"),
                new University("연성대학교", "yeonsung.ac.kr"),
                new University("전주대학교", "jj.ac.kr"),
                new University("서일대학교", "seoil.ac.kr"),
                new University("인덕대학교", "induk.ac.kr"),
                new University("고려사이버대학교", "cuk.edu"),
                new University("영남이공대학교", "ync.ac.kr"),
                new University("장안대학교", "jangan.ac.kr"),
                new University("순천향대학교", "sch.ac.kr"),
                new University("백석문화대학교", "bscu.ac.kr"),
                new University("계명문화대학교", "kmcu.ac.kr"),
                new University("경남대학교", "kyungnam.ac.kr"),
                new University("남서울대학교", "nsu.ac.kr"),
                new University("오산대학교", "osan.ac.kr"),
                new University("세종사이버대학교", "sjcu.ac.kr"),
                new University("제주대학교", "jejunu.ac.kr"),
                new University("경복대학교", "kbu.ac.kr"),
                new University("마산대학교", "masan.ac.kr"),
                new University("수원대학교", "suwon.ac.kr"),
                new University("상지대학교", "sangji.ac.kr"),
                new University("수원과학대학교", "ssc.ac.kr"),
                new University("동서대학교", "dongseo.ac.kr"),
                new University("대전보건대학교", "hit.ac.kr"),
                new University("선문대학교", "sunmoon.ac.kr"),
                new University("유한대학교", "yuhan.ac.kr"),
                new University("경인여자대학교", "kic.ac.kr"),
                new University("배재대학교", "pcu.ac.kr"),
                new University("서영대학교", "seoyoung.ac.kr"),
                new University("우송대학교", "wsu.ac.kr"),
                new University("대전대학교", "dju.ac.kr"),
                new University("중부대학교", "jmail.ac.kr"),
                new University("한국교통대학교", "ut.ac.kr"),
                new University("인제대학교", "inje.ac.kr"),
                new University("동의과학대학교", "dit.ac.kr"),
                new University("한밭대학교", "hanbat.ac.kr"),
                new University("한성대학교", "hansung.ac.kr"),
                new University("삼육대학교", "syu.ac.kr"),
                new University("한국항공대학교", "kau.ac.kr"),
                new University("서울예술대학교", "seoularts.ac.kr"),
                new University("부산대학교", "pusan.ac.kr"),
                new University("상명대학교", "smu.ac.kr")
        );

        universityRepository.saveAll(universities);
        log.info("Successfully initialized {} universities based on the original enum data.", universities.size());
    }

    @Transactional
    public void clearUniversities() {
        universityRepository.deleteAll();
        log.info("All universities have been deleted.");
    }
}
