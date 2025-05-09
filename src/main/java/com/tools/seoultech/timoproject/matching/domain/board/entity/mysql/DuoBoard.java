package com.tools.seoultech.timoproject.matching.domain.board.entity.mysql;

import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.user.entity.mysql.BaseUser;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DiscriminatorValue("Duo-Type")
@PrimaryKeyJoinColumn(name = "board_id")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuoBoard extends BaseBoard {
    private DuoMapCode duoMapCode;

    @Builder
    public DuoBoard(BaseUser baseUser, String memo, DuoMapCode duoMapCode) {
        super(baseUser, memo);
        this.duoMapCode = duoMapCode;
    }
}
