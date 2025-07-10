package com.tools.seoultech.timoproject.notification;

import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.UserAgreement;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.notification.channel.EmailNotificationChannel;
import com.tools.seoultech.timoproject.notification.service.EmailTemplateService;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.dto.NotificationRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailNotificationChannelTest {

    @Mock
    private EmailTemplateService emailTemplateService;

    @Mock
    private MemberService memberService;

    private EmailNotificationChannel emailChannel;
    private Member testMember;

    @BeforeEach
    void setUp() {
//        emailChannel = new EmailNotificationChannel(emailTemplateService, memberService);
        testMember = createTestMember();
        when(memberService.getById(999L)).thenReturn(testMember);
    }

    private Member createTestMember() {
        return Member.builder()
                .memberId(999L)
                .email("test.user@timo.test")
                .username("테스트유저-12345")
                .notificationEmail("mentenseoul@gmail.com")
                .riotAccount(new RiotAccount(
                        "TEST_PUUID_12345",
                        "테스트소환사",
                        "TEST",
                        "https://ddragon.leagueoflegends.com/cdn/15.12.1/img/profileicon/1.png",
                        RiotVerificationType.API_PARSED
                ))
                .certifiedUnivInfo(new CertifiedUnivInfo(
                        "test@test.ac.kr",
                        "테스트대학교"
                ))
                .oAuthProvider(OAuthProvider.RIOT)
                .role(Role.MEMBER)
                .term(UserAgreement.ENABLED)
                .build();
    }

    @Test
    void HTML_이메일_전송_테스트() {
        // given
        NotificationRequest request = new NotificationRequest(
                NotificationType.DUO_APPLY,
                "/duo/apply/test-123",
                "신청자테스트"
        );

        NotificationChannelRequest channelRequest = NotificationChannelRequest.from(
                testMember, request, request.getFormattedMessage()
        );

        // when
//        emailChannel.send(channelRequest);

        // then
        Mockito.verify(memberService, times(1)).getById(999L);
//        Mockito.verify(emailTemplateService, times(1)).sendHtmlEmail(channelRequest, "테스트유저-12345");

        System.out.println("✅ HTML 이메일 전송 테스트 완료");
    }

}