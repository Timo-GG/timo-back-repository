package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.Redis_RankingInfo;
import com.tools.seoultech.timoproject.riot.dto.RiotRankingDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingRedisService {
	private static final String RANKING_KEY = "lol:ranking";
	private static final String RANKING_OBJECT_KEY = "lol:ranking:objects";

	private final MemberAccountRepository memberAccountRepository;
	private final RankingInfoRepository rankingInfoRepository;

	@Qualifier("rankingRedisTemplate")
	private final RedisTemplate<String, Object> redisTemplate;

	public void createInitialRanking(Long memberId, RiotRankingDto riotRankingDto) {
		MemberAccount account = memberAccountRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		if (account.getRiotAccount() == null || account.getCertifiedUnivInfo() == null) {
			throw new BusinessException(ErrorCode.INVALID_RANKING_INFO);
		}

		Redis_RankingInfo rankingInfo = Redis_RankingInfo.from(memberId, account, riotRankingDto);
		rankingInfoRepository.findByMemberAccountMemberId(memberId)
			.ifPresent(existing -> {
				String department = account.getCertifiedUnivInfo().getDepartment();
				RankingUpdateRequestDto updateRequestDto = RankingUpdateRequestDto.fromEntity(existing, department);
				rankingInfo.updateRankingInfo(updateRequestDto);
			});

		saveRankInfo(memberId, rankingInfo);
		log.info("[Redis 등록 완료] memberId={}, score={}", memberId, rankingInfo.getScore());
	}

	public void saveRankInfo(Long memberId, Redis_RankingInfo rankingInfo) {
		int score = rankingInfo.getScore();
		String memberIdStr = memberId.toString();
		String universityKey = buildUniversityKey(rankingInfo.getUniversity());

		redisTemplate.opsForZSet().add(RANKING_KEY, memberIdStr, score);
		redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberIdStr, rankingInfo);
		redisTemplate.opsForZSet().add(universityKey, memberIdStr, score);

		log.info("랭킹 저장 완료: memberId={}, university={}", memberId, rankingInfo.getUniversity());
	}

	public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
		Redis_RankingInfo existing = (Redis_RankingInfo)redisTemplate.opsForHash()
			.get(RANKING_OBJECT_KEY, memberId.toString());

		if (existing == null) {
			log.warn("업데이트 실패: Redis 랭킹 정보 없음, memberId={}", memberId);
			return;
		}

		existing.updateRankingInfo(dto);
		redisTemplate.opsForHash().put(RANKING_OBJECT_KEY, memberId.toString(), existing);
		log.info("유저 정보 업데이트 완료: memberId={}", memberId);
	}

	public List<Redis_RankingInfo> getTopRankingsByUniversity(String university, int limit) {
		return getTopRankings(buildUniversityKey(university), limit);
	}

	public List<Redis_RankingInfo> getTopRankings(int limit) {
		return getTopRankings(RANKING_KEY, limit);
	}

	private List<Redis_RankingInfo> getTopRankings(String key, int limit) {
		Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);

		if (topMembers == null || topMembers.isEmpty()) {
			return Collections.emptyList();
		}

		List<Redis_RankingInfo> result = new ArrayList<>();
		for (Object memberId : topMembers) {
			Redis_RankingInfo rankInfo = (Redis_RankingInfo)redisTemplate.opsForHash()
				.get(RANKING_OBJECT_KEY, memberId.toString());
			if (rankInfo != null) {
				result.add(rankInfo);
			}
		}
		return result;
	}

	public Redis_RankingInfo getMyRankingInfo(Long memberId) {
		Redis_RankingInfo rankInfo = (Redis_RankingInfo)redisTemplate.opsForHash()
			.get(RANKING_OBJECT_KEY, memberId.toString());

		if (rankInfo == null) {
			throw new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND);
		}
		return rankInfo;
	}

	public void flushAllRankingData() {
		redisTemplate.delete(RANKING_KEY);
		redisTemplate.delete(RANKING_OBJECT_KEY);
		Set<String> univKeys = redisTemplate.keys("lol:ranking:univ:*");
		if (univKeys != null && !univKeys.isEmpty()) {
			redisTemplate.delete(univKeys);
		}
		log.info("모든 랭킹 데이터 플러시 완료");
	}

	public void deleteRankingByMemberId(Long memberId) {
		String id = memberId.toString();

		Redis_RankingInfo info = (Redis_RankingInfo)redisTemplate.opsForHash()
			.get(RANKING_OBJECT_KEY, id);
		if (info != null) {
			String univKey = buildUniversityKey(info.getUniversity());
			redisTemplate.opsForZSet().remove(univKey, id);
		}

		redisTemplate.opsForZSet().remove(RANKING_KEY, id);
		redisTemplate.opsForHash().delete(RANKING_OBJECT_KEY, id);

		log.info("랭킹 삭제 완료: memberId={}", memberId);
	}

	private String buildUniversityKey(String university) {
		try {
			String encoded = URLEncoder.encode(university.trim(), StandardCharsets.UTF_8.toString());
			return "lol:ranking:univ:" + encoded;
		} catch (Exception e) {
			log.warn("대학교 키 인코딩 실패, 원본 사용: {}", university, e);
			return "lol:ranking:univ:" + university.trim();
		}
	}
}
