package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCertifiedUnivInfo is a Querydsl query type for CertifiedUnivInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCertifiedUnivInfo extends BeanPath<CertifiedUnivInfo> {

    private static final long serialVersionUID = 349579131L;

    public static final QCertifiedUnivInfo certifiedUnivInfo = new QCertifiedUnivInfo("certifiedUnivInfo");

    public final StringPath department = createString("department");

    public final StringPath univCertifiedEmail = createString("univCertifiedEmail");

    public final StringPath univName = createString("univName");

    public QCertifiedUnivInfo(String variable) {
        super(CertifiedUnivInfo.class, forVariable(variable));
    }

    public QCertifiedUnivInfo(Path<? extends CertifiedUnivInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCertifiedUnivInfo(PathMetadata metadata) {
        super(CertifiedUnivInfo.class, metadata);
    }

}

