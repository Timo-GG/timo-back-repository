package com.tools.seoultech.timoproject.matching.board.entity.mysql;

import com.tools.seoultech.timoproject.matching.user.entity.mysql.BaseUser;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DiscriminatorValue("Duo-Type")
@PrimaryKeyJoinColumn(name = "board_id")
@Entity
@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class DuoSearchBoard extends BaseSearchBoard{
    @Builder
    public DuoSearchBoard(BaseUser baseUser, String memo) {
        super(baseUser, memo);
    }
}
