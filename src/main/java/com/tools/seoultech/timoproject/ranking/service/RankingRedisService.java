package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
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

	private final MemberRepository memberRepository;
	private final RankingInfoRedisRepository rankingInfoRedisRepository;

	@Qualifier("rankingRedisTemplate")
	private final RedisTemplate<String, Object> redisTemplate;

	public void createInitialRanking(Long memberId, RiotRankingDto riotRankingDto) {
		Member account = memberRepository.findById(memberId)
				.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		RedisRankingInfo rankingInfo = RedisRankingInfo.from(memberId, account, riotRankingDto);
		rankingInfo.updateId(memberId);

		rankingInfoRedisRepository.save(rankingInfo);

		saveRankInfo(memberId, rankingInfo);
		log.info("[Redis 등록 완료] memberId={}, score={}", memberId, rankingInfo.getScore());
	}

	public void saveRankInfo(Long memberId, RedisRankingInfo rankingInfo) {
		int score = rankingInfo.getScore();
		String id = memberId.toString();
		String universityKey = buildUniversityKey(rankingInfo.getUniversity());

		redisTemplate.opsForZSet().add(RANKING_KEY, id, score);
		redisTemplate.opsForZSet().add(universityKey, id, score);

		log.info("랭킹 저장 완료: memberId={}, university={}", memberId, rankingInfo.getUniversity());
	}

	public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
		RedisRankingInfo existing = rankingInfoRedisRepository.findById(memberId.toString())
				.orElseThrow(() -> new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND));
		RedisRankingInfo updated = RedisRankingInfo.updateUserInfo(existing, dto);
		rankingInfoRedisRepository.save(updated);

		log.info("유저 정보 업데이트 완료: memberId={}", memberId);
	}

	public List<RedisRankingInfo> getTopRankings(String key, int offset, int limit) {
		long start = offset;
		long end = offset + limit - 1;

		Set<Object> topMembers = redisTemplate.opsForZSet().reverseRange(key, start, end);

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

	public List<RedisRankingInfo> getTopRankings(int offset, int limit) {
		return getTopRankings(RANKING_KEY, offset, limit);
	}

	public List<RedisRankingInfo> getTopRankingsByUniversity(String university, int offset, int limit) {
		return getTopRankings(buildUniversityKey(university), offset, limit);
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

	public long getTotalRankingCount() {
		return Optional.ofNullable(redisTemplate.opsForZSet().size(RANKING_KEY)).orElse(0L);
	}

	public long getTotalRankingCountByUniversity(String university) {
		String key = buildUniversityKey(university);
		return Optional.ofNullable(redisTemplate.opsForZSet().size(key)).orElse(0L);
	}

	public int getRankingPosition(String name, String tag) {
		// 공백 제거 및 대소문자 무시 처리
		String normalizedName = name.trim();
		String normalizedTag = tag.trim();

		// Redis에서 전부 가져와서 소환사 정보 매칭
		List<RedisRankingInfo> allRankings = getTopRankings(0, Integer.MAX_VALUE);
		for (int i = 0; i < allRankings.size(); i++) {
			RedisRankingInfo info = allRankings.get(i);
			if (info.getGameName().equalsIgnoreCase(normalizedName) &&
					info.getTagLine().replace(" ", "").equalsIgnoreCase(normalizedTag.replace(" ", ""))) {
				return i + 1; // 순위는 1부터 시작
			}
		}

		throw new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND);
	}

	public void updateRankingFromRiotAPI(Long memberId, RiotRankingDto riotRankingDto) {
		RedisRankingInfo existing = rankingInfoRedisRepository.findById(memberId.toString())
				.orElseThrow(() -> new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND));
		RedisRankingInfo updated = RedisRankingInfo.updateFromRiotAPI(existing, riotRankingDto);
		rankingInfoRedisRepository.save(updated);
		saveRankInfo(memberId, updated);

		log.info("Riot API 데이터로 랭킹 업데이트 완료: memberId={}, newScore={}",
				memberId, updated.getScore());
	}

	public void updateVerificationType(Long memberId, String verificationType) {
		RedisRankingInfo existing = rankingInfoRedisRepository.findById(memberId.toString())
				.orElseThrow(() -> new BusinessException(ErrorCode.REDIS_RANKING_NOT_FOUND));
		RedisRankingInfo updated = RedisRankingInfo.updateVerificationType(existing, verificationType);
		rankingInfoRedisRepository.save(updated);

		log.info("verificationType 업데이트 완료: memberId={}, verificationType={}",
				memberId, verificationType);
	}
}
