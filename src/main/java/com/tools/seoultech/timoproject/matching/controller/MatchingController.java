package com.tools.seoultech.timoproject.matching.controller;


import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.service.facade.MatchingFacade;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/matching/event")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Matching API")
public class MatchingController {
    private final MatchingFacade matchingFacade;

    @GetMapping("/match/{myPageUUID}")
    public ResponseEntity<APIDataResponse<MyPageDTO.Response>> matchingEvent(@PathVariable UUID myPageUUID) throws Exception {
        var dto = matchingFacade.doAcceptEvent(myPageUUID);
        return ResponseEntity.ok(APIDataResponse.of(dto));
    }

    @GetMapping("/reject/{myPageUUID}")
    public ResponseEntity<Void> requestEvent(@PathVariable UUID myPageUUID) throws Exception {
        matchingFacade.doRejectEvent(myPageUUID);
        return ResponseEntity.noContent().build();
    }
}
