package com.tools.seoultech.timoproject.version2.matching.board.entity;

import com.tools.seoultech.timoproject.version2.matching.user.entity.BaseUserEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
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
    public DuoSearchBoard(BaseUserEntity baseUserEntity, String memo) {
        super(baseUserEntity, memo);
    }
}
