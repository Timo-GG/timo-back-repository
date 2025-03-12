package com.tools.seoultech.timoproject.policy.controller;

import com.tools.seoultech.timoproject.global.APIErrorResponse;
import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.policy.service.PolicyService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/policy")
@RequiredArgsConstructor
public class PolicyAPIController {
    private final PolicyService policyService;
    @GetMapping
    public APIDataResponse create(@CurrentMemberId Long memberId){
        policyService.save(memberId);
        return APIDataResponse.empty();
    }
    @DeleteMapping
    public APIDataResponse delete(@CurrentMemberId Long memberId){
        policyService.delete(memberId);
        return APIDataResponse.empty();
    }
    @GetMapping("/isExpired")
    public APIDataResponse expiredCheck(@CurrentMemberId Long memberId){
        return policyService.isExpired(memberId) ?
                APIDataResponse.of(
                        false,
                        APIErrorResponse.of(
                                false,
                                ErrorCode.VALIDATION_ERROR,
                                "만기되었습니다."
                        )) :
                APIDataResponse.empty();
    }
}
