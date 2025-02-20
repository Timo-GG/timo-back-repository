package com.tools.seoultech.timoproject.member.facade;

import com.tools.seoultech.timoproject.member.dto.MemberInfoResponse;

public interface MemberFacade {

    MemberInfoResponse getMemberInfo(Long memberId);

}
