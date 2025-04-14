package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompactPlayerHistory {
    /*
    * 해딩 필드 타입들은 RSO 정보 받아와야알 수 있음.
    **/
    private String rankTier;
    private String tierStep;

    private List<String> most3Champ;
    private List<String> last4Match;
}
