package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;

@Embeddable
public class CertifiedUnivInfo {
    @Email
    @Column(nullable = false, unique = true)
    private String univCertifiedEmail;

    @Column(nullable = false)
    private String department;
}
