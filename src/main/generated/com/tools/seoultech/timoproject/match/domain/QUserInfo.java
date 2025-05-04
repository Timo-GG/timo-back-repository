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

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition> playCondition = createEnum("playCondition", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> playPosition = createEnum("playPosition", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle> playStyle = createEnum("playStyle", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat> voiceChat = createEnum("voiceChat", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat.class);

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

