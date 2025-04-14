package com.tools.seoultech.timoproject.version2.ranking.controller;


import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ranking")
public class RankingController {

    @PostMapping("/me")
    public ResponseEntity<?> updateMyProfile(){

        return ResponseEntity.ok(APIDataResponse.of("ok"));
    }
}
