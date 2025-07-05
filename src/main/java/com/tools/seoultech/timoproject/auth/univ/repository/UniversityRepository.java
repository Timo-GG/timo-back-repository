package com.tools.seoultech.timoproject.auth.univ.repository;

import com.tools.seoultech.timoproject.auth.univ.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByName(String name);
}
