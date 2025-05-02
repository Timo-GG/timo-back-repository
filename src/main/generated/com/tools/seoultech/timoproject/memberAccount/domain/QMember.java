package com.tools.seoultech.timoproject.memberAccount.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -115006272L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final ListPath<com.tools.seoultech.timoproject.post.domain.entity.Comment, com.tools.seoultech.timoproject.post.domain.entity.QComment> comments = this.<com.tools.seoultech.timoproject.post.domain.entity.Comment, com.tools.seoultech.timoproject.post.domain.entity.QComment>createList("comments", com.tools.seoultech.timoproject.post.domain.entity.Comment.class, com.tools.seoultech.timoproject.post.domain.entity.QComment.class, PathInits.DIRECT2);

    public final com.tools.seoultech.timoproject.match.domain.QDuoInfo duoInfo;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath nickname = createString("nickname");

    public final EnumPath<OAuthProvider> oAuthProvider = createEnum("oAuthProvider", OAuthProvider.class);

    public final StringPath playerName = createString("playerName");

    public final StringPath playerTag = createString("playerTag");

    public final ListPath<com.tools.seoultech.timoproject.post.domain.entity.Post, com.tools.seoultech.timoproject.post.domain.entity.QPost> posts = this.<com.tools.seoultech.timoproject.post.domain.entity.Post, com.tools.seoultech.timoproject.post.domain.entity.QPost>createList("posts", com.tools.seoultech.timoproject.post.domain.entity.Post.class, com.tools.seoultech.timoproject.post.domain.entity.QPost.class, PathInits.DIRECT2);

    public final NumberPath<Integer> profileImageId = createNumber("profileImageId", Integer.class);

    public final ListPath<com.tools.seoultech.timoproject.rating.Rating, com.tools.seoultech.timoproject.rating.QRating> ratings = this.<com.tools.seoultech.timoproject.rating.Rating, com.tools.seoultech.timoproject.rating.QRating>createList("ratings", com.tools.seoultech.timoproject.rating.Rating.class, com.tools.seoultech.timoproject.rating.QRating.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    public final com.tools.seoultech.timoproject.match.domain.QUserInfo userInfo;

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.duoInfo = inits.isInitialized("duoInfo") ? new com.tools.seoultech.timoproject.match.domain.QDuoInfo(forProperty("duoInfo")) : null;
        this.userInfo = inits.isInitialized("userInfo") ? new com.tools.seoultech.timoproject.match.domain.QUserInfo(forProperty("userInfo")) : null;
    }

}

