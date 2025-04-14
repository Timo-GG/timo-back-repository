package com.tools.seoultech.timoproject.version2.matching.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDuoUserEntity is a Querydsl query type for DuoUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDuoUserEntity extends EntityPathBase<DuoUserEntity> {

    private static final long serialVersionUID = -544556809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDuoUserEntity duoUserEntity = new QDuoUserEntity("duoUserEntity");

    public final QBaseUserEntity _super;

    public final com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QDuoInfo_Ver2 duoInfo;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount memberAccount;

    public final com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QUserInfo_Ver2 userInfo;

    public QDuoUserEntity(String variable) {
        this(DuoUserEntity.class, forVariable(variable), INITS);
    }

    public QDuoUserEntity(Path<? extends DuoUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDuoUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDuoUserEntity(PathMetadata metadata, PathInits inits) {
        this(DuoUserEntity.class, metadata, inits);
    }

    public QDuoUserEntity(Class<? extends DuoUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseUserEntity(type, metadata, inits);
        this.duoInfo = inits.isInitialized("duoInfo") ? new com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QDuoInfo_Ver2(forProperty("duoInfo")) : null;
        this.id = _super.id;
        this.memberAccount = _super.memberAccount;
        this.userInfo = inits.isInitialized("userInfo") ? new com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QUserInfo_Ver2(forProperty("userInfo")) : null;
    }

}

