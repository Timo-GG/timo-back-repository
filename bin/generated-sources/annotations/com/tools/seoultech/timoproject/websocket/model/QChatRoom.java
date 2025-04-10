package com.tools.seoultech.timoproject.websocket.model;

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

    private static final long serialVersionUID = 5858447L;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final ListPath<ChatMessage, QChatMessage> chatMessages = this.<ChatMessage, QChatMessage>createList("chatMessages", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    public final ListPath<ChatRoomMember, QChatRoomMember> chatRoomMembers = this.<ChatRoomMember, QChatRoomMember>createList("chatRoomMembers", ChatRoomMember.class, QChatRoomMember.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isGroupChat = createString("isGroupChat");

    public final BooleanPath isTerminated = createBoolean("isTerminated");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

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

