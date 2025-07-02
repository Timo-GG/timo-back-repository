package com.tools.seoultech.timoproject.riot.utils;

import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;

public final class RiotAccountUtil {

    private RiotAccountUtil() {
    }

    public static String extractGameName(RiotAccount riotAccount) {
        if (riotAccount == null) {
            return "알 수 없는 사용자";  // null 대신 기본값 반환
        }

        String gameName = riotAccount.getGameName();
        String tagLine = riotAccount.getTagLine();

        if (gameName == null || tagLine == null) {
            return "알 수 없는 사용자";  // null 대신 기본값 반환
        }

        return gameName + "#" + tagLine;
    }

    public static String extractGameNameFromMemberInfo(CertifiedMemberInfo memberInfo) {
        if (memberInfo == null) {
            return "알 수 없는 사용자";
        }
        return extractGameName(memberInfo.getRiotAccount());
    }
}