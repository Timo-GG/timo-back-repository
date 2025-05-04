package com.tools.seoultech.timoproject.match.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDuoInfo is a Querydsl query type for DuoInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDuoInfo extends EntityPathBase<DuoInfo> {

    private static final long serialVersionUID = 1941722555L;

    public static final QDuoInfo duoInfo = new QDuoInfo("duoInfo");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> duoPlayPosition = createEnum("duoPlayPosition", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle> duoPlayStyle = createEnum("duoPlayStyle", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QDuoInfo(String variable) {
        super(DuoInfo.class, forVariable(variable));
    }

    public QDuoInfo(Path<? extends DuoInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDuoInfo(PathMetadata metadata) {
        super(DuoInfo.class, metadata);
    }

}

