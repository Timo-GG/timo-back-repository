<<<<<<<< HEAD:src/main/java/com/tools/seoultech/timoproject/version2/matching/domain/board/entity/mysql/DuoSearchBoard.java
package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.mysql;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql.BaseUser;
========
package com.tools.seoultech.timoproject.matching.board.entity.mysql;

import com.tools.seoultech.timoproject.matching.user.entity.mysql.BaseUser;
>>>>>>>> develop:src/main/java/com/tools/seoultech/timoproject/matching/board/entity/mysql/DuoSearchBoard.java
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
