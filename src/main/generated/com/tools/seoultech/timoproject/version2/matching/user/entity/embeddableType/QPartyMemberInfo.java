package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPartyMemberInfo is a Querydsl query type for PartyMemberInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPartyMemberInfo extends BeanPath<PartyMemberInfo> {

    private static final long serialVersionUID = 710416820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPartyMemberInfo partyMemberInfo = new QPartyMemberInfo("partyMemberInfo");

    public final QCompactPlayerHistory compactPlayerHistory;

    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QRiotAccount riotAccount;

    public final QUserInfo_Ver2 userInfo;

    public QPartyMemberInfo(String variable) {
        this(PartyMemberInfo.class, forVariable(variable), INITS);
    }

    public QPartyMemberInfo(Path<? extends PartyMemberInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPartyMemberInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPartyMemberInfo(PathMetadata metadata, PathInits inits) {
        this(PartyMemberInfo.class, metadata, inits);
    }

    public QPartyMemberInfo(Class<? extends PartyMemberInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.compactPlayerHistory = inits.isInitialized("compactPlayerHistory") ? new QCompactPlayerHistory(forProperty("compactPlayerHistory")) : null;
        this.riotAccount = inits.isInitialized("riotAccount") ? new com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QRiotAccount(forProperty("riotAccount")) : null;
        this.userInfo = inits.isInitialized("userInfo") ? new QUserInfo_Ver2(forProperty("userInfo")) : null;
    }

}

