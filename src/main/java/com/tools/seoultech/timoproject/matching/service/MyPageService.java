package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository.PageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisDuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.RedisDuoPageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.RedisScrimPageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.PageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import com.tools.seoultech.timoproject.matching.service.facade.Impl.BoardFacadeImpl;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final BoardFacadeImpl board;
    private final BoardService boardService;
    private final RiotAPIService bas;
    private final MemberService memberService;

    private final RedisDuoPageRepository redisDuoPageRepository;
    private final RedisScrimPageRepository redisScrimPageRepository;
    private final PageRepository pageRepository;

    private final MyPageMapper myPageMapper;

    /**
     * Redis 엔티티 관련 MyPage 서비스
     * @apiNote [Create] : MyPage 엔티티 생성. <br>
     *          [Read] : UUID, MatchingCategory별 조회. <br>
     *          [Update] : 업데이트 기능 없음. Matching Service 로직을 통해 자동으로 활용됨.<br>
     *          [Delete] : UUID, MatchingCategory별 삭제.
     */
    // Note: MyPage 엔티티 생성
    public RedisDuoPage createDuoMyPage(MatchingDTO.RequestDuo dto) throws Exception {
        return redisDuoPageRepository.save(myPageMapper.toDuoRedis(dto, boardService, bas, memberService));
    }

    public RedisScrimPage createScrimMyPage(MatchingDTO.RequestScrim dto) throws Exception {
        return redisScrimPageRepository.save(myPageMapper.toScrimMyPage(dto, boardService, bas, memberService));
    }

    // Note: MyPage UUID 조회
    public RedisDuoPageOnly readDuoMyPage(UUID myPageUUID) throws Exception {
        RedisDuoPageOnly proj = redisDuoPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 레디스 저장소에 없습니다."));
        return proj;
    }

    public RedisScrimPageOnly readScrimMyPage(UUID myPageUUID) throws Exception {
        RedisScrimPageOnly proj = redisScrimPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 레디스 저장소에 없습니다."));
        return proj;
    }

    public PageOnly getMyPage(UUID myPageUUID) throws Exception {
        PageOnly proj = redisDuoPageRepository.findByMyPageUUID(myPageUUID)
                .map(p -> (PageOnly) p)
                .or(() -> redisScrimPageRepository.findByMyPageUUID(myPageUUID)
                        .map(p -> (PageOnly) p))
                .orElseThrow(() -> new Exception("Board not found: " + myPageUUID));
        return proj;
    }

    // TODO : requestorId로 모든 페이지 조회하기 -> 임시 코드
    public List<PageOnly> getAllMyPageByAcceptorRaw(Long acceptorId) {
        List<PageOnly> result = new ArrayList<>();
        result.addAll(redisDuoPageRepository.findAllByAcceptorId(acceptorId));
        result.addAll(redisScrimPageRepository.findByAcceptorId(acceptorId));
        return result;
    }

    // TODO : acceptorId로 모든 페이지 조회하기 -> 임시 코드
    public List<PageOnly> getAllMyPageByRequestorRaw(Long requestorId) {
        List<PageOnly> result = new ArrayList<>();
        result.addAll(redisDuoPageRepository.findAllByRequestorId(requestorId));
        result.addAll(redisScrimPageRepository.findByRequestorId(requestorId));
        return result;
    }

    public List<RedisDuoPageOnly> getAllDuoMyPage() throws Exception {
        return redisDuoPageRepository.findAllBy();
    }

    public List<RedisScrimPageOnly> getAllScrimMyPage() throws Exception {
        return redisScrimPageRepository.findAllBy();
    }

    public boolean existsDuoPageBy(Long memberId, UUID boardUUID) {
        return redisDuoPageRepository.findByRequestorIdAndBoardUUID(memberId, boardUUID).isPresent();
    }

    // 특정 게시글에 이미 신청했는지 확인 (내전)
    public boolean existsScrimPageBy(Long memberId, UUID boardUUID) {
        return redisScrimPageRepository.findByRequestorIdAndBoardUUID(memberId, boardUUID).isPresent();
    }

    // 통합 확인 메서드
    public boolean existsPageBy(Long memberId, UUID boardUUID) {
        boolean duoExists = existsDuoPageBy(memberId, boardUUID);
        boolean scrimExists = existsScrimPageBy(memberId, boardUUID);
        return duoExists || scrimExists;
    }

    /** MyPage UUID 삭제 */
    public void deleteDuoMyPage(UUID BoardUUID) {
        redisDuoPageRepository.deleteById(BoardUUID);
    }

    public void deleteScrimMyPage(UUID BoardUUID) {
        redisScrimPageRepository.deleteById(BoardUUID);
    }

    public void deleteAllDuoMyPage() {
        redisDuoPageRepository.deleteAll();
    }

    public void deleteAllScrimMyPage() {
        redisScrimPageRepository.deleteAll();
    }



    /**
     * MySQL 엔티티 관련 MyPage 서비스
     * @apiNote [Create] : <Strong>백엔드 내부 테스트용</Strong> MyPage 엔티티 생성. <br>
     *          [Read] : UUID, 조건별 조회. <br>
     *          [Update] : 업데이트 기능 없음. Matching Service 로직을 통해 자동으로 활용됨.<br>
     *          [Delete] : UUID, MatchingCategory별 삭제.
     */
    // Note: MySQL 엔티티 생성.
    public DuoPage createDuoPage(UUID myPageUUID) throws Exception{
        RedisDuoPageOnly proj = redisDuoPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 myPageUUId는 없습니다."));

        return pageRepository.save(myPageMapper.toDuoEntity(proj, memberService));
    }

    public ScrimPage createScrimPage(UUID myPageUUID){
        RedisScrimPageOnly proj = redisScrimPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 myPageUUId는 없습니다."));
        return pageRepository.save(myPageMapper.toScrimEntity(proj, memberService));
    }

    // Note: MySQL 엔티티 조회
    public DuoPage readDuoPage(Long mypageId) throws Exception {
        DuoPage entity = (DuoPage) pageRepository.findById(mypageId)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 DB 저장소에 없습니다."));
        return entity;
    }

    public ScrimPage readScrimPage(Long mypageId) throws Exception {
        ScrimPage entity = (ScrimPage) pageRepository.findById(mypageId)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 DB 저장소에 없습니다."));
        return entity;
    }

    public MyPage readPage(Long mypageId) throws Exception {
            return pageRepository.findById(mypageId)
                    .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 DB 저장소에 없습니다."));
    }

    public List<MyPage> readAllPage() throws Exception {
        List<MyPage> entity = pageRepository.findAll();
        return entity;
    }

    // Sorting and Filtering
    // Note: 보낸 요청, 받은 요청 페이지 조회
    public List<MyPage> readPageReceived(Long memberId) throws Exception {
        return pageRepository.findAllByAcceptorId(memberId);
    }

    public List<MyPage> readPageSent(Long memberId) throws Exception {
        return pageRepository.findAllByRequestorId(memberId);
    }

    public Map<Boolean, List<MyPage>> readPageSortingByIsReceived(Long memberId) throws Exception {
        List<MyPage> receivedEntity = pageRepository.findAllByAcceptorId(memberId);
        List<MyPage> sentEntity = pageRepository.findAllByRequestorId(memberId);

        Boolean isAcceptor = true;
        return Map.of(isAcceptor, receivedEntity, !isAcceptor, sentEntity);
    }

    // Note: MySQL 엔티티 삭제
    public void deletePage(Long mypageId){
        pageRepository.deleteById(mypageId);
    }


    public void updateVerificationTypeInMyPages(Long memberId, String verificationType) {
        log.info("🔄 MyPage 인증 타입 업데이트 시작: memberId={}, type={}",
                memberId, verificationType);

        int duoUpdated = updateDuoPageVerification(memberId, verificationType);
        int scrimUpdated = updateScrimPageVerification(memberId, verificationType);

        log.info("✅ MyPage 인증 타입 업데이트 완료: memberId={}, duo={}, scrim={}",
                memberId, duoUpdated, scrimUpdated);
    }

    private int updateDuoPageVerification(Long memberId, String verificationType) {
        int updateCount = 0;

        try {
            List<RedisDuoPageOnly> acceptorPages = redisDuoPageRepository.findAllByAcceptorId(memberId);
            for (RedisDuoPageOnly pageProjection : acceptorPages) {
                RedisDuoPage updated = RedisDuoPage.updateAcceptorVerificationFromProjection(
                        pageProjection, verificationType);
                redisDuoPageRepository.save(updated);
                updateCount++;
            }

            List<RedisDuoPageOnly> requestorPages = redisDuoPageRepository.findAllByRequestorId(memberId);
            for (RedisDuoPageOnly pageProjection : requestorPages) {
                RedisDuoPage updated = RedisDuoPage.updateRequestorVerificationFromProjection(
                        pageProjection, verificationType);
                redisDuoPageRepository.save(updated);
                updateCount++;
            }

            log.debug("✅ Duo 페이지 업데이트 완료: count={}", updateCount);

        } catch (Exception e) {
            log.error("❌ Duo 페이지 인증 타입 업데이트 실패: memberId={}", memberId, e);
            throw e;
        }

        return updateCount;
    }

    private int updateScrimPageVerification(Long memberId, String verificationType) {
        int updateCount = 0;

        try {
            // Acceptor로 참여한 페이지들
            List<RedisScrimPageOnly> acceptorPages = redisScrimPageRepository.findByAcceptorId(memberId);
            for (RedisScrimPageOnly pageProjection : acceptorPages) {
                // Projection → Entity 변환 후 업데이트
                RedisScrimPage updated = RedisScrimPage.updateAcceptorVerificationFromProjection(
                        pageProjection, verificationType);
                redisScrimPageRepository.save(updated);
                updateCount++;
            }

            List<RedisScrimPageOnly> requestorPages = redisScrimPageRepository.findByRequestorId(memberId);
            for (RedisScrimPageOnly pageProjection : requestorPages) {
                // Projection → Entity 변환 후 업데이트
                RedisScrimPage updated = RedisScrimPage.updateRequestorVerificationFromProjection(
                        pageProjection, verificationType);
                redisScrimPageRepository.save(updated);
                updateCount++;
            }

            log.debug("✅ Scrim 페이지 업데이트 완료: count={}", updateCount);

        } catch (Exception e) {
            log.error("❌ Scrim 페이지 인증 타입 업데이트 실패: memberId={}", memberId, e);
            throw e;
        }

        return updateCount;
    }

}
