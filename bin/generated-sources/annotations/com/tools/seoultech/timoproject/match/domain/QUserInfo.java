package com.tools.seoultech.timoproject.match.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInfo is a Querydsl query type for UserInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserInfo extends EntityPathBase<UserInfo> {

    private static final long serialVersionUID = 1897299722L;

    public static final QUserInfo userInfo = new QUserInfo("userInfo");

    public final EnumPath<GameMode> gameMode = createEnum("gameMode", GameMode.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final EnumPath<PlayCondition> playCondition = createEnum("playCondition", PlayCondition.class);

    public final EnumPath<PlayPosition> playPosition = createEnum("playPosition", PlayPosition.class);

    public final EnumPath<PlayStyle> playStyle = createEnum("playStyle", PlayStyle.class);

    public final EnumPath<VoiceChat> voiceChat = createEnum("voiceChat", VoiceChat.class);

    public QUserInfo(String variable) {
        super(UserInfo.class, forVariable(variable));
    }

    public QUserInfo(Path<? extends UserInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfo(PathMetadata metadata) {
        super(UserInfo.class, metadata);
    }

}

