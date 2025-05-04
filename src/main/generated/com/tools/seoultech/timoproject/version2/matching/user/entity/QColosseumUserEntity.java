package com.tools.seoultech.timoproject.version2.matching.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QColosseumUserEntity is a Querydsl query type for ColosseumUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QColosseumUserEntity extends EntityPathBase<ColosseumUserEntity> {

    private static final long serialVersionUID = -441296761L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QColosseumUserEntity colosseumUserEntity = new QColosseumUserEntity("colosseumUserEntity");

    public final QBaseUserEntity _super;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.QMemberAccount memberAccount;

    public final ListPath<com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.PartyMemberInfo, com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QPartyMemberInfo> partyMemberList = this.<com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.PartyMemberInfo, com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QPartyMemberInfo>createList("partyMemberList", com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.PartyMemberInfo.class, com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.QPartyMemberInfo.class, PathInits.DIRECT2);

    public QColosseumUserEntity(String variable) {
        this(ColosseumUserEntity.class, forVariable(variable), INITS);
    }

    public QColosseumUserEntity(Path<? extends ColosseumUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QColosseumUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QColosseumUserEntity(PathMetadata metadata, PathInits inits) {
        this(ColosseumUserEntity.class, metadata, inits);
    }

    public QColosseumUserEntity(Class<? extends ColosseumUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseUserEntity(type, metadata, inits);
        this.id = _super.id;
        this.memberAccount = _super.memberAccount;
    }

}

