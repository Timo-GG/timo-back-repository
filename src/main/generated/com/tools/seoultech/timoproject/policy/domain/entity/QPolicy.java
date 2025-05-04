package com.tools.seoultech.timoproject.policy.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPolicy is a Querydsl query type for Policy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPolicy extends EntityPathBase<Policy> {

    private static final long serialVersionUID = 656560479L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPolicy policy = new QPolicy("policy");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final BooleanPath collectingAgreement = createBoolean("collectingAgreement");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tools.seoultech.timoproject.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final BooleanPath usingAgreement = createBoolean("usingAgreement");

    public QPolicy(String variable) {
        this(Policy.class, forVariable(variable), INITS);
    }

    public QPolicy(Path<? extends Policy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPolicy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPolicy(PathMetadata metadata, PathInits inits) {
        this(Policy.class, metadata, inits);
    }

    public QPolicy(Class<? extends Policy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.tools.seoultech.timoproject.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

