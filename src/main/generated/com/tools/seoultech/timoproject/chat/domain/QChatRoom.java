package com.tools.seoultech.timoproject.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = 1332497847L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final ListPath<ChatRoomMember, QChatRoomMember> chatRoomMembers = this.<ChatRoomMember, QChatRoomMember>createList("chatRoomMembers", ChatRoomMember.class, QChatRoomMember.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isGroupChat = createBoolean("isGroupChat");

    public final BooleanPath isTerminated = createBoolean("isTerminated");

    public final StringPath lastMessageContent = createString("lastMessageContent");

    public final NumberPath<Long> lastMessageSenderId = createNumber("lastMessageSenderId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastMessageTime = createDateTime("lastMessageTime", java.time.LocalDateTime.class);

    public final StringPath matchId = createString("matchId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QChatRoom(String variable) {
        super(ChatRoom.class, forVariable(variable));
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatRoom(PathMetadata metadata) {
        super(ChatRoom.class, metadata);
    }

}

