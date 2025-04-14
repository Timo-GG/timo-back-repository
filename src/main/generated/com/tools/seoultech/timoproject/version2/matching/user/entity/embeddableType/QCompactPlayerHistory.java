package com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompactPlayerHistory is a Querydsl query type for CompactPlayerHistory
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCompactPlayerHistory extends BeanPath<CompactPlayerHistory> {

    private static final long serialVersionUID = -577150966L;

    public static final QCompactPlayerHistory compactPlayerHistory = new QCompactPlayerHistory("compactPlayerHistory");

    public final ListPath<String, StringPath> last4Match = this.<String, StringPath>createList("last4Match", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> most3Champ = this.<String, StringPath>createList("most3Champ", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath rankTier = createString("rankTier");

    public final StringPath tierStep = createString("tierStep");

    public QCompactPlayerHistory(String variable) {
        super(CompactPlayerHistory.class, forVariable(variable));
    }

    public QCompactPlayerHistory(Path<? extends CompactPlayerHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompactPlayerHistory(PathMetadata metadata) {
        super(CompactPlayerHistory.class, metadata);
    }

}

