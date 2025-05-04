package com.tools.seoultech.timoproject.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminLog is a Querydsl query type for AdminLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminLog extends EntityPathBase<AdminLog> {

    private static final long serialVersionUID = 1729876804L;

    public static final QAdminLog adminLog = new QAdminLog("adminLog");

    public final com.tools.seoultech.timoproject.global.QBaseEntity _super = new com.tools.seoultech.timoproject.global.QBaseEntity(this);

    public final EnumPath<AdminLog.EntityType> entityType = createEnum("entityType", AdminLog.EntityType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<AdminLog.MethodType> methodType = createEnum("methodType", AdminLog.MethodType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath performedBy = createString("performedBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath targetId = createString("targetId");

    public QAdminLog(String variable) {
        super(AdminLog.class, forVariable(variable));
    }

    public QAdminLog(Path<? extends AdminLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminLog(PathMetadata metadata) {
        super(AdminLog.class, metadata);
    }

}

