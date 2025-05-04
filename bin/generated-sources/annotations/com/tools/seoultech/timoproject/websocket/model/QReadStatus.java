package com.tools.seoultech.timoproject.websocket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReadStatus is a Querydsl query type for ReadStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReadStatus extends EntityPathBase<ReadStatus> {

    private static final long serialVersionUID = -1767623068L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReadStatus readStatus = new QReadStatus("readStatus");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final QChatMessage chatMessage;

    public final QChatRoom chatRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final com.tools.seoultech.timoproject.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QReadStatus(String variable) {
        this(ReadStatus.class, forVariable(variable), INITS);
    }

    public QReadStatus(Path<? extends ReadStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReadStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReadStatus(PathMetadata metadata, PathInits inits) {
        this(ReadStatus.class, metadata, inits);
    }

    public QReadStatus(Class<? extends ReadStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatMessage = inits.isInitialized("chatMessage") ? new QChatMessage(forProperty("chatMessage"), inits.get("chatMessage")) : null;
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.tools.seoultech.timoproject.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

