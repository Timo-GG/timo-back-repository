package com.tools.seoultech.timoproject.version2.matching.board.entity;


import com.tools.seoultech.timoproject.version2.matching.user.entity.BaseUserEntity;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
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
public class ColloseumSearchBoard extends BaseSearchBoard{
    @Builder
    public ColloseumSearchBoard(BaseUserEntity baseUserEntity, String memo) {
        super(baseUserEntity, memo);
    }
}
