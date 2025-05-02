package com.tools.seoultech.timoproject.matching.myPage.entity.EnumType;

public enum MatchingStatus {
    WAITING,           // 수락 대기.
    CONNECTED,        // 완료 대기.
    DISCONNECTED,    // 방 폭파.
    COMPLETED;      // 완료 확인

    public boolean isExistInRedis(MatchingStatus matchingStatus){
        switch(matchingStatus){
            case WAITING:
            case CONNECTED:
            case DISCONNECTED:
                return true;

            default:
                return false;
        }
    }
}
