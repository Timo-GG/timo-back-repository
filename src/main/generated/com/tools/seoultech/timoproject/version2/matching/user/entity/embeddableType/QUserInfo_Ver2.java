package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInfo_Ver2 is a Querydsl query type for UserInfo_Ver2
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserInfo_Ver2 extends BeanPath<UserInfo_Ver2> {

    private static final long serialVersionUID = -764823621L;

    public static final QUserInfo_Ver2 userInfo_Ver2 = new QUserInfo_Ver2("userInfo_Ver2");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> myPosition = createEnum("myPosition", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition> myStatus = createEnum("myStatus", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle> myStyle = createEnum("myStyle", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat> myVoice = createEnum("myVoice", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat.class);

    public QUserInfo_Ver2(String variable) {
        super(UserInfo_Ver2.class, forVariable(variable));
    }

    public QUserInfo_Ver2(Path<? extends UserInfo_Ver2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfo_Ver2(PathMetadata metadata) {
        super(UserInfo_Ver2.class, metadata);
    }

}

