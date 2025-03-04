package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.member.dto.AccountDto;
import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;

public interface MemberFacade {

    MemberInfoResponse getMemberInfo(Long memberId);

    AccountDto.Response verifyPlayer(AccountDto.Request request);

    boolean checkNickname(String nickname);

    String createRandomNickname();

    MemberInfoResponse updateMemberInfo(Long memberId, UpdateMemberInfoRequest request);

    Integer updateProfileImageId(Long memberId, Integer imageId);
}
