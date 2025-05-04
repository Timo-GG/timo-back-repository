package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDuoInfo_Ver2 is a Querydsl query type for DuoInfo_Ver2
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDuoInfo_Ver2 extends BeanPath<DuoInfo_Ver2> {

    private static final long serialVersionUID = 1893903260L;

    public static final QDuoInfo_Ver2 duoInfo_Ver2 = new QDuoInfo_Ver2("duoInfo_Ver2");

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition> opponentPosition = createEnum("opponentPosition", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition.class);

    public final EnumPath<com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle> opponentStyle = createEnum("opponentStyle", com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle.class);

    public QDuoInfo_Ver2(String variable) {
        super(DuoInfo_Ver2.class, forVariable(variable));
    }

    public QDuoInfo_Ver2(Path<? extends DuoInfo_Ver2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDuoInfo_Ver2(PathMetadata metadata) {
        super(DuoInfo_Ver2.class, metadata);
    }

}

