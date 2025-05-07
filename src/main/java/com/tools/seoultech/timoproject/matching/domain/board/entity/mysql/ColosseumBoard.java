package com.tools.seoultech.timoproject.matching.domain.board.entity.mysql;


import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
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
public class ColosseumBoard extends BaseBoard {
    private ColosseumMapCode mapCode;
    private Integer headCount;

    @Builder
    public ColosseumBoard(BaseUser baseUser, String memo, ColosseumMapCode mapCode, Integer headCount) {
        super(baseUser, memo);
        this.mapCode = mapCode;
        this.headCount = headCount;
    }
}
