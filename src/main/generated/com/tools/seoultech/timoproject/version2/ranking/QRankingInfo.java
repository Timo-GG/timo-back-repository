package com.tools.seoultech.timoproject.version2.ranking;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRankingInfo is a Querydsl query type for RankingInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRankingInfo extends EntityPathBase<RankingInfo> {

    private static final long serialVersionUID = -1195550584L;

    public static final QRankingInfo rankingInfo = new QRankingInfo("rankingInfo");

    public final ComparablePath<com.nimbusds.openid.connect.sdk.claims.Gender> gender = createComparable("gender", com.nimbusds.openid.connect.sdk.claims.Gender.class);

    public final StringPath mbti = createString("mbti");

    public final StringPath memo = createString("memo");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> position = createEnum("position", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final NumberPath<Long> rankId = createNumber("rankId", Long.class);

    public QRankingInfo(String variable) {
        super(RankingInfo.class, forVariable(variable));
    }

    public QRankingInfo(Path<? extends RankingInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRankingInfo(PathMetadata metadata) {
        super(RankingInfo.class, metadata);
    }

}

