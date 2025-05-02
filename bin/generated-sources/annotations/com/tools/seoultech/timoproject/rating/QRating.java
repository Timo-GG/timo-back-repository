package com.tools.seoultech.timoproject.rating;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRating is a Querydsl query type for Rating
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRating extends EntityPathBase<Rating> {

    private static final long serialVersionUID = 460354366L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRating rating = new QRating("rating");

    public final EnumPath<Rating.Attitude> attitude = createEnum("attitude", Rating.Attitude.class);

    public final com.tools.seoultech.timoproject.memberAccount.domain.QMember duo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tools.seoultech.timoproject.memberAccount.domain.QMember member;

    public final NumberPath<java.math.BigDecimal> score = createNumber("score", java.math.BigDecimal.class);

    public final EnumPath<Rating.Skill> skill = createEnum("skill", Rating.Skill.class);

    public final EnumPath<Rating.Speech> speech = createEnum("speech", Rating.Speech.class);

    public QRating(String variable) {
        this(Rating.class, forVariable(variable), INITS);
    }

    public QRating(Path<? extends Rating> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRating(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRating(PathMetadata metadata, PathInits inits) {
        this(Rating.class, metadata, inits);
    }

    public QRating(Class<? extends Rating> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.duo = inits.isInitialized("duo") ? new com.tools.seoultech.timoproject.memberAccount.domain.QMember(forProperty("duo"), inits.get("duo")) : null;
        this.member = inits.isInitialized("member") ? new com.tools.seoultech.timoproject.memberAccount.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

