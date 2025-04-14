package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertifiedUnivInfo {
    @Email
    @Column(nullable = false, unique = true)
    private String univCertifiedEmail;

    @Column(nullable = false)
    private String department;
}
