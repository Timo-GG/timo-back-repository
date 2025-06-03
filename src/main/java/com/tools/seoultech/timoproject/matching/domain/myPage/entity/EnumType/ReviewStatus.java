package com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType;

import lombok.Getter;

@Getter
public enum ReviewStatus {
    UNREVIEWED, ACCEPTOR_REVIEWED, REQUESTOR_REVIEWED, BOTH_REVIEWED;

    public ReviewStatus nextStatus(Boolean IsUpdatedWithAcceptor) {
        return switch (this) {
            case UNREVIEWED -> IsUpdatedWithAcceptor ? ACCEPTOR_REVIEWED : REQUESTOR_REVIEWED;
            case ACCEPTOR_REVIEWED -> IsUpdatedWithAcceptor ? ACCEPTOR_REVIEWED : BOTH_REVIEWED;
            case REQUESTOR_REVIEWED -> IsUpdatedWithAcceptor ? BOTH_REVIEWED : REQUESTOR_REVIEWED;
            case BOTH_REVIEWED -> BOTH_REVIEWED;
            default -> throw new IllegalArgumentException("ReviewStatus의 nextStatus 에서 target 타입이 잘못되었습니다.");
        };
    }
}
