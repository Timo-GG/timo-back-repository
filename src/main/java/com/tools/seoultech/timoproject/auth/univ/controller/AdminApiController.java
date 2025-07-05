package com.tools.seoultech.timoproject.auth.univ.controller;

import com.tools.seoultech.timoproject.auth.univ.service.UniversitySetupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "[Admin] UnivApi", description = "Admin Univ API")
public class AdminApiController {

    private final UniversitySetupService universitySetupService;

    @PostMapping("/universities/init")
    public ResponseEntity<String> initializeUniversities() {
        universitySetupService.initializeUniversities();
        return ResponseEntity.ok("University data initialization triggered.");
    }

    @PostMapping("/universities/clear")
    public ResponseEntity<String> clearUniversities() {
        universitySetupService.clearUniversities();
        return ResponseEntity.ok("University data cleared.");
    }
}