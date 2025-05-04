package com.tools.seoultech.timoproject.version2.matching.myPage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1198788547L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentAttitude> attitude_score = createEnum("attitude_score", com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentAttitude.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentConversation> conversation_score = createEnum("conversation_score", com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentConversation.class);

    public final NumberPath<Integer> evaluation_score = createNumber("evaluation_score", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount member;

    public final StringPath memo = createString("memo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentTalent> talent_score = createEnum("talent_score", com.tools.seoultech.timoproject.version2.matching.myPage.entity.EnumType.OpponentTalent.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount(forProperty("member"), inits.get("member")) : null;
    }

}

