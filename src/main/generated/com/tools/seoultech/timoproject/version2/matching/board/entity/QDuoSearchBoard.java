package com.tools.seoultech.timoproject.version2.matching.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDuoSearchBoard is a Querydsl query type for DuoSearchBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDuoSearchBoard extends EntityPathBase<DuoSearchBoard> {

    private static final long serialVersionUID = 1901583990L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDuoSearchBoard duoSearchBoard = new QDuoSearchBoard("duoSearchBoard");

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

    public QDuoSearchBoard(String variable) {
        this(DuoSearchBoard.class, forVariable(variable), INITS);
    }

    public QDuoSearchBoard(Path<? extends DuoSearchBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDuoSearchBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDuoSearchBoard(PathMetadata metadata, PathInits inits) {
        this(DuoSearchBoard.class, metadata, inits);
    }

    public QDuoSearchBoard(Class<? extends DuoSearchBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBaseSearchBoard(type, metadata, inits);
        this.baseUser = _super.baseUser;
        this.id = _super.id;
        this.memo = _super.memo;
        this.modDate = _super.modDate;
        this.regDate = _super.regDate;
    }

}

