package com.tools.seoultech.timoproject.version2.matching.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QColloseumSearchBoard is a Querydsl query type for ColloseumSearchBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QColloseumSearchBoard extends EntityPathBase<ColloseumSearchBoard> {

    private static final long serialVersionUID = 605971111L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QColloseumSearchBoard colloseumSearchBoard = new QColloseumSearchBoard("colloseumSearchBoard");

    public final QBaseSearchBoard _super;

    // inherited
    public final com.tools.seoultech.timoproject.version2.matching.user.entity.QBaseUserEntity baseUser;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final StringPath memo;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate;

    public QColloseumSearchBoard(String variable) {
        this(ColloseumSearchBoard.class, forVariable(variable), INITS);
    }

    public QColloseumSearchBoard(Path<? extends ColloseumSearchBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QColloseumSearchBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QColloseumSearchBoard(PathMetadata metadata, PathInits inits) {
        this(ColloseumSearchBoard.class, metadata, inits);
    }

    public QColloseumSearchBoard(Class<? extends ColloseumSearchBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseSearchBoard(type, metadata, inits);
        this.baseUser = _super.baseUser;
        this.id = _super.id;
        this.memo = _super.memo;
        this.modDate = _super.modDate;
        this.regDate = _super.regDate;
    }

}

