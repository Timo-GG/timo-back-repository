package com.tools.seoultech.timoproject.version2.ranking;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRankingInfo is a Querydsl query type for RankingInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRankingInfo extends EntityPathBase<RankingInfo> {

    private static final long serialVersionUID = -1195550584L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRankingInfo rankingInfo = new QRankingInfo("rankingInfo");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.Gender> gender = createEnum("gender", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.Gender.class);

    public final StringPath mbti = createString("mbti");

    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount memberAccount;

    public final StringPath memo = createString("memo");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> position = createEnum("position", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final NumberPath<Long> rankId = createNumber("rankId", Long.class);

    public QRankingInfo(String variable) {
        this(RankingInfo.class, forVariable(variable), INITS);
    }

    public QRankingInfo(Path<? extends RankingInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRankingInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRankingInfo(PathMetadata metadata, PathInits inits) {
        this(RankingInfo.class, metadata, inits);
    }

    public QRankingInfo(Class<? extends RankingInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberAccount = inits.isInitialized("memberAccount") ? new com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount(forProperty("memberAccount"), inits.get("memberAccount")) : null;
    }

}

