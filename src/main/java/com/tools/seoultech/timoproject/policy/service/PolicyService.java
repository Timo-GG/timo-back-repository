package com.tools.seoultech.timoproject.policy.service;

public interface PolicyService {
    void save(Long memberId);
    void delete(Long memberId);
    boolean isExpired(Long memberId);
}
