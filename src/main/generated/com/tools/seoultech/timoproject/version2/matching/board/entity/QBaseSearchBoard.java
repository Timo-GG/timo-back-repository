package com.tools.seoultech.timoproject.version2.matching.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBaseSearchBoard is a Querydsl query type for BaseSearchBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBaseSearchBoard extends EntityPathBase<BaseSearchBoard> {

    private static final long serialVersionUID = 1908377623L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBaseSearchBoard baseSearchBoard = new QBaseSearchBoard("baseSearchBoard");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final com.tools.seoultech.timoproject.version2.matching.user.entity.QBaseUserEntity baseUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QBaseSearchBoard(String variable) {
        this(BaseSearchBoard.class, forVariable(variable), INITS);
    }

    public QBaseSearchBoard(Path<? extends BaseSearchBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBaseSearchBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBaseSearchBoard(PathMetadata metadata, PathInits inits) {
        this(BaseSearchBoard.class, metadata, inits);
    }

    public QBaseSearchBoard(Class<? extends BaseSearchBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.baseUser = inits.isInitialized("baseUser") ? new com.tools.seoultech.timoproject.version2.matching.user.entity.QBaseUserEntity(forProperty("baseUser"), inits.get("baseUser")) : null;
    }

}

