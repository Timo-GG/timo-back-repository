package com.tools.seoultech.timoproject.member.domain.entity.embeddableType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    public void updateDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("학과명은 비어 있을 수 없습니다.");
        }
        this.department = department;
    }
}
