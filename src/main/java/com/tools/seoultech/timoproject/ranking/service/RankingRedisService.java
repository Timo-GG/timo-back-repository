package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.ranking.RankingInfoRedisRepository;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import com.tools.seoultech.timoproject.ranking.dto.RedisRankingInfo;
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

	private final MemberAccountRepository memberAccountRepository;
	private final RankingInfoRedisRepository rankingInfoRedisRepository;

	@Qualifier("rankingRedisTemplate")
	private final RedisTemplate<String, Object> redisTemplate;

	public void createInitialRanking(Long memberId, RiotRankingDto riotRankingDto) {
		MemberAccount account = memberAccountRepository.findById(memberId)
				.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		RedisRankingInfo rankingInfo = RedisRankingInfo.from(memberId, account, riotRankingDto);
		rankingInfo.updateId(memberId);

		rankingInfoRedisRepository.save(rankingInfo);

		saveRankInfo(memberId, rankingInfo);
		log.info("[Redis 등록 완료] memberId={}, score={}", memberId, rankingInfo.getScore());
	}

	public void saveRankInfo(Long memberId, RedisRankingInfo rankingInfo) {
		int score = rankingInfo.getScore();
		String memberIdStr = memberId.toString();
		String universityKey = buildUniversityKey(rankingInfo.getUniversity());

		redisTemplate.opsForZSet().add(RANKING_KEY, memberIdStr, score);
		redisTemplate.opsForZSet().add(universityKey, memberIdStr, score);

		log.info("랭킹 저장 완료: memberId={}, university={}", memberId, rankingInfo.getUniversity());
	}

	public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
		RedisRankingInfo existing = rankingInfoRedisRepository.findById(memberId.toString())
				.orElseThrow(() -> new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND));

		existing.updateRankingInfo(dto);
		rankingInfoRedisRepository.save(existing);
		log.info("유저 정보 업데이트 완료: memberId={}", memberId);
	}

	public List<RedisRankingInfo> getTopRankingsByUniversity(String university, int limit) {
		return getTopRankings(buildUniversityKey(university), limit);
	}

	public List<RedisRankingInfo> getTopRankings(int limit) {
		return getTopRankings(RANKING_KEY, limit);
	}

	private List<RedisRankingInfo> getTopRankings(String key, int limit) {
		Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(key, 0, limit - 1);

		if (topMembers == null || topMembers.isEmpty()) {
			return Collections.emptyList();
		}

		List<RedisRankingInfo> result = new ArrayList<>();
		for (Object memberId : topMembers) {
			rankingInfoRedisRepository.findById(memberId.toString())
					.ifPresent(result::add);
		}
		return result;
	}

	public RedisRankingInfo getMyRankingInfo(Long memberId) {
		return rankingInfoRedisRepository.findById(memberId.toString())
				.orElseThrow(() -> new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND));
	}

	public void flushAllRankingData() {
		redisTemplate.delete(RANKING_KEY);
		Set<String> univKeys = redisTemplate.keys("lol:ranking:univ:*");
		if (univKeys != null && !univKeys.isEmpty()) {
			redisTemplate.delete(univKeys);
		}
		rankingInfoRedisRepository.deleteAll();
		log.info("모든 랭킹 데이터 플러시 완료");
	}

	public void deleteRankingByMemberId(Long memberId) {
		String id = memberId.toString();

		RedisRankingInfo info = rankingInfoRedisRepository.findById(id)
				.orElse(null);
		if (info != null) {
			String univKey = buildUniversityKey(info.getUniversity());
			redisTemplate.opsForZSet().remove(univKey, id);
		}

		redisTemplate.opsForZSet().remove(RANKING_KEY, id);
		rankingInfoRedisRepository.deleteById(id);

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
