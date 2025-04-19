package com.tools.seoultech.timoproject.version2.matching.myPage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyPageBoard is a Querydsl query type for MyPageBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyPageBoard extends EntityPathBase<MyPageBoard> {

    private static final long serialVersionUID = 343957382L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyPageBoard myPageBoard = new QMyPageBoard("myPageBoard");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final com.tools.seoultech.timoproject.version2.matching.user.entity.mysql.QBaseUser acceptor;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final com.tools.seoultech.timoproject.version2.matching.user.entity.mysql.QBaseUser requestor;

    public QMyPageBoard(String variable) {
        this(MyPageBoard.class, forVariable(variable), INITS);
    }

    public QMyPageBoard(Path<? extends MyPageBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyPageBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyPageBoard(PathMetadata metadata, PathInits inits) {
        this(MyPageBoard.class, metadata, inits);
    }

    public QMyPageBoard(Class<? extends MyPageBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.acceptor = inits.isInitialized("acceptor") ? new com.tools.seoultech.timoproject.version2.matching.user.entity.mysql.QBaseUser(forProperty("acceptor"), inits.get("acceptor")) : null;
        this.requestor = inits.isInitialized("requestor") ? new com.tools.seoultech.timoproject.version2.matching.user.entity.mysql.QBaseUser(forProperty("requestor"), inits.get("requestor")) : null;
    }

}

