package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class CertifiedUnivInfo {
    @Email
    @Column(nullable = true, unique = true)
    private String univCertifiedEmail;

    @Column(nullable = true)
    private String univName;

    @Column(nullable = true)
    private String department;

    public CertifiedUnivInfo(String univCertifiedEmail, String univName) {
        this.univCertifiedEmail = univCertifiedEmail;
        this.univName = univName;
    }
}
