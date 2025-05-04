package com.tools.seoultech.timoproject.version2.matching.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaseUserEntity is a Querydsl query type for BaseUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBaseUserEntity extends EntityPathBase<BaseUserEntity> {

    private static final long serialVersionUID = 1405853140L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBaseUserEntity baseUserEntity = new QBaseUserEntity("baseUserEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount memberAccount;

    public QBaseUserEntity(String variable) {
        this(BaseUserEntity.class, forVariable(variable), INITS);
    }

    public QBaseUserEntity(Path<? extends BaseUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaseUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaseUserEntity(PathMetadata metadata, PathInits inits) {
        this(BaseUserEntity.class, metadata, inits);
    }

    public QBaseUserEntity(Class<? extends BaseUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberAccount = inits.isInitialized("memberAccount") ? new com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount(forProperty("memberAccount"), inits.get("memberAccount")) : null;
    }

}

