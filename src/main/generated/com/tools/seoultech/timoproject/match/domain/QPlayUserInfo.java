package com.tools.seoultech.timoproject.match.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlayUserInfo is a Querydsl query type for PlayUserInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPlayUserInfo extends BeanPath<PlayUserInfo> {

    private static final long serialVersionUID = -571368994L;

    public static final QPlayUserInfo playUserInfo = new QPlayUserInfo("playUserInfo");

    public final StringPath puuid = createString("puuid");

    public final StringPath userName = createString("userName");

    public final StringPath userTag = createString("userTag");

    public QPlayUserInfo(String variable) {
        super(PlayUserInfo.class, forVariable(variable));
    }

    public QPlayUserInfo(Path<? extends PlayUserInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlayUserInfo(PathMetadata metadata) {
        super(PlayUserInfo.class, metadata);
    }

}

