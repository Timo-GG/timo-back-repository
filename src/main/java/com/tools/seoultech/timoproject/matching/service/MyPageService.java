package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.BoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.repository.PageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.ScrimMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.DuoMyPageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.ScrimMyPageRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.DuoMyPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.MyPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.ScrimMyPageOnly;
import com.tools.seoultech.timoproject.matching.service.facade.Impl.BoardFacadeImpl;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final BoardFacadeImpl board;
    private final BoardService boardService;
    private final BasicAPIService bas;
    private final MemberService memberService;

    private final DuoMyPageRepository duoMyPageRepository;
    private final ScrimMyPageRepository scrimMyPageRepository;
    private final PageRepository pageRepository;

    private final MyPageMapper myPageMapper;

    /** MyPage 엔티티 생성 */
    // Redis 엔티티 생성.
    public DuoMyPage createDuoMyPage(MatchingDTO.RequestDuo dto) throws Exception {
        return duoMyPageRepository.save(myPageMapper.toDuoRedis(dto, boardService, bas, memberService));
    }

    public ScrimMyPage createScrimMyPage(MatchingDTO.RequestScrim dto) throws Exception {
        return scrimMyPageRepository.save(myPageMapper.toScrimMyPage(dto, boardService, bas, memberService));
    }

    // MySQL 엔티티 생성.
    public DuoPage createDuoPage(UUID myPageUUID) throws Exception{
        DuoMyPageOnly proj = duoMyPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 myPageUUId는 없습니다."));

        return pageRepository.save(myPageMapper.toDuoEntity(proj, memberService));
    }

    public ScrimPage createScrimPage(UUID myPageUUID){
        ScrimMyPageOnly proj = scrimMyPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 myPageUUId는 없습니다."));

        return pageRepository.save(myPageMapper.toScrimEntity(proj, memberService));
    }

    /** MyPage UUID 조회 */
    // Redis 엔티티 조회
    public DuoMyPageOnly readDuoMyPage(UUID myPageUUID) throws Exception {
        DuoMyPageOnly proj = duoMyPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 레디스 저장소에 없습니다."));
        return proj;
    }

    public ScrimMyPageOnly readScrimMyPage(UUID myPageUUID) throws Exception {
        ScrimMyPageOnly proj = scrimMyPageRepository.findByMyPageUUID(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 레디스 저장소에 없습니다."));
        return proj;
    }

    public MyPageOnly getBoard(UUID boardUUID) throws Exception {
        MyPageOnly proj = duoMyPageRepository.findByMyPageUUID(boardUUID)
                .map(p -> (MyPageOnly) p)
                .or(() -> scrimMyPageRepository.findByMyPageUUID(boardUUID)
                        .map(p -> (MyPageOnly) p))
                .orElseThrow(() -> new Exception("Board not found: " + boardUUID));
        return proj;
    }

    public List<DuoMyPageOnly> getAllDuoMyPage() throws Exception {
        return duoMyPageRepository.findAllBy();
    }

    public List<ScrimMyPageOnly> getAllScrimMyPage() throws Exception {
        return scrimMyPageRepository.findAllBy();
    }

    // MySQL 엔티티 조회
    public DuoPage readDuoMyPage(Long mypageId) throws Exception {
        DuoPage entity = (DuoPage) pageRepository.findById(mypageId)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 DB 저장소에 없습니다."));
        return entity;
    }

    public ScrimPage readScrimMyPage(Long mypageId) throws Exception {
        ScrimPage entity = (ScrimPage) pageRepository.findById(mypageId)
                .orElseThrow(() -> new GeneralException("해당 UUID의 MyPage가 DB 저장소에 없습니다."));
        return entity;
    }

    // 보낸 요청, 받은 요청 페이지 조회
    public List<MyPage> readMyPageReceived(Long memberId) throws Exception {
        return pageRepository.findAllByAcceptorId(memberId);
    }

    public List<MyPage> readMyPageSent(Long memberId) throws Exception {
        return pageRepository.findAllByRequestorId(memberId);
    }

    public List<MyPageDTO.Response> readMyPageSortingByIsReceived(Long memberId) throws Exception {
        List<MyPage> receivedEntity = pageRepository.findAllByRequestorId(memberId);
        List<MyPage> sentEntity = pageRepository.findAllByAcceptorId(memberId);

        List<MyPageDTO.Response> allDtoList =
                Stream.concat(
                                receivedEntity.stream().map(entity -> Map.entry(entity, true)),
                                sentEntity.stream().map(entity -> Map.entry(entity, false))
                )
                .map(entry -> myPageMapper.toFilteredDtoList(entry.getKey(), entry.getValue()))
                .toList();
        return allDtoList;
    }

    /** MyPage UUID 삭제 */
    // Redis 엔티티 삭제
    public void deleteDuoMyPage(UUID BoardUUID) {
        duoMyPageRepository.deleteById(BoardUUID);
    }

    public void deleteScrimMyPage(UUID BoardUUID) {
        scrimMyPageRepository.deleteById(BoardUUID);
    }

    public void deleteAllDuoMyPage() {
        duoMyPageRepository.deleteAll();
    }

    public void deleteAllScrimMyPage() {
        scrimMyPageRepository.deleteAll();
    }

    // MySQL 엔티티 삭제
    public void deleteDuoPage(Long mypageId){
        pageRepository.deleteById(mypageId);
    }

    public void deleteScrimPage(Long mypageId){
        pageRepository.deleteById(mypageId);
    }
}
