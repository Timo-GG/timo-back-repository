package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAccount is a Querydsl query type for MemberAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAccount extends EntityPathBase<MemberAccount> {

    private static final long serialVersionUID = -781468589L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAccount memberAccount = new QMemberAccount("memberAccount");

    public final QCertifiedUnivInfo certifiedUnivInfo;

    public final StringPath email = createString("email");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final EnumPath<com.tools.seoultech.timoproject.member.domain.OAuthProvider> oAuthProvider = createEnum("oAuthProvider", com.tools.seoultech.timoproject.member.domain.OAuthProvider.class);

    public final QRiotAccount riotAccount;

    public final EnumPath<com.tools.seoultech.timoproject.member.domain.Role> role = createEnum("role", com.tools.seoultech.timoproject.member.domain.Role.class);

    public final StringPath username = createString("username");

    public QMemberAccount(String variable) {
        this(MemberAccount.class, forVariable(variable), INITS);
    }

    public QMemberAccount(Path<? extends MemberAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAccount(PathMetadata metadata, PathInits inits) {
        this(MemberAccount.class, metadata, inits);
    }

    public QMemberAccount(Class<? extends MemberAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.certifiedUnivInfo = inits.isInitialized("certifiedUnivInfo") ? new QCertifiedUnivInfo(forProperty("certifiedUnivInfo")) : null;
        this.riotAccount = inits.isInitialized("riotAccount") ? new QRiotAccount(forProperty("riotAccount")) : null;
    }

}

