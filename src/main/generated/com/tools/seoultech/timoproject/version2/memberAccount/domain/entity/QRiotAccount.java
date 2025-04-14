package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRiotAccount is a Querydsl query type for RiotAccount
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRiotAccount extends BeanPath<RiotAccount> {

    private static final long serialVersionUID = 630654545L;

    public static final QRiotAccount riotAccount = new QRiotAccount("riotAccount");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountTag = createString("accountTag");

    public final StringPath puuid = createString("puuid");

    public QRiotAccount(String variable) {
        super(RiotAccount.class, forVariable(variable));
    }

    public QRiotAccount(Path<? extends RiotAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRiotAccount(PathMetadata metadata) {
        super(RiotAccount.class, metadata);
    }

}

