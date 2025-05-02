<<<<<<<< HEAD:src/main/java/com/tools/seoultech/timoproject/version2/matching/domain/user/entity/mysql/BaseUser.java
package com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql;
========
package com.tools.seoultech.timoproject.matching.user.entity.mysql;
>>>>>>>> develop:src/main/java/com/tools/seoultech/timoproject/matching/user/entity/mysql/BaseUser.java

import com.tools.seoultech.timoproject.memberAccount.domain.MemberAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="member_account_id")
    private MemberAccount memberAccount;

    protected BaseUser(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }
}
