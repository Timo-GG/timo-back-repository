package com.tools.seoultech.timoproject.member.domain.entity.enumType;

public enum RiotVerificationType {
    NONE, // 초기 상태: 라이엇 계정이 없는 상태
    API_PARSED,    // 기존 방식: API 파싱으로 검증
    RSO_VERIFIED   // 새로운 방식: RSO로 본인 인증
}
