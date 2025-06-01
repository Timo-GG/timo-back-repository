package com.tools.seoultech.timoproject.matching.domain.board.repository.projections;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;

import java.util.List;
import java.util.UUID;

public interface BoardOnly {
    UUID getBoardUUID();

    /** CertifiedMemberInfo */
    String getPuuid();
    String getGameName();
    String getTagLine();

    /** RankInfoDto 일부 */
    String getTier();   // GOLD, PLATINUM, MASTER 등
    String getRank();

    /** CompactMemberInfo 나머지 필드 */
    List<String> getMost3Champ();
    PlayPosition getMyPosition();

    /** CertifiedMemberInfo 나머지 필드 */
    String getUnivName();
    String getDepartment();
    Gender getGender();
    String getMbti();
    String getProfileUrl();
}
