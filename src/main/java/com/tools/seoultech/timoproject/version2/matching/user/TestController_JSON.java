package com.tools.seoultech.timoproject.version2.matching.user;

import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import com.tools.seoultech.timoproject.version2.matching.board.entity.DuoSearchBoard;
import com.tools.seoultech.timoproject.version2.matching.user.entity.BaseUserEntity;
import com.tools.seoultech.timoproject.version2.matching.user.entity.DuoUserEntity;
import com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.DuoInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.user.entity.embeddableType.UserInfo_Ver2;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayCondition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayStyle;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.VoiceChat;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test/json")
@RequiredArgsConstructor
public class TestController_JSON {
    private final MemberAccount member = MemberAccount.builder()
            .memberId(1l)
            .userName("티모대위#1235653")
            .riotAccount(new RiotAccount("puuid", "롤찍먹만할게요", "#5103"))
            .certifiedUnivInfo(new CertifiedUnivInfo("louis5103@seoultech.ac.kr","의과대학"))
            .build();

    private final DuoUserEntity user = DuoUserEntity.builder()
            .member(member)
            .duoInfo(new DuoInfo_Ver2(PlayPosition.MID, PlayStyle.FUN))
            .userInfo(new UserInfo_Ver2(PlayPosition.JUNGLE, PlayStyle.FUN,PlayCondition.FIRST, VoiceChat.DISABLED))
            .build();

    @GetMapping("/memberAccount")
    public ResponseEntity<APIDataResponse<MemberAccount>> getMemberAccount() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(member));
    }
    @GetMapping("/DuoSearchBoard")
    public ResponseEntity<APIDataResponse<DuoSearchBoard>> getDuoSearchBoard() {

        DuoSearchBoard duoBoard = DuoSearchBoard.builder()
                .baseUserEntity(user)
                .memo("memo")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(duoBoard));
    }
    @GetMapping("/DuoUserEntity")
    public ResponseEntity<APIDataResponse<DuoUserEntity>> getDuoUserEntity() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(APIDataResponse.of(user));
    }
}
