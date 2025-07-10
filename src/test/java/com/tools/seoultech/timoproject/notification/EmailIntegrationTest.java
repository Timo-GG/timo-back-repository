package com.tools.seoultech.timoproject.notification;
import com.tools.seoultech.timoproject.notification.service.EmailTemplateService;
import com.tools.seoultech.timoproject.notification.dto.NotificationChannelRequest;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class EmailIntegrationTest {

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Test
    void 실제_HTML_이메일_전송_테스트() {
        // given
        NotificationChannelRequest request = new NotificationChannelRequest(
                999L,
                "mentenseoul@gmail.com",
                NotificationType.DUO_APPLY,
                "🎮 테스트유저님이 듀오 신청하였습니다.",
                "/duo/apply/test-123"
        );

        // when
//        emailTemplateService.sendHtmlEmail(request, "테스트유저");

        // then
        System.out.println("✅ 실제 이메일 전송 완료!");
        System.out.println("   mentenseoul@gmail.com 받은편지함을 확인해보세요.");
        System.out.println("   스팸 폴더도 꼭 확인해보세요!");
    }

    @Test
    void 간단한_텍스트_이메일_테스트() {
        try {
            // JavaMailSender를 직접 사용한 간단한 테스트
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mentenseoul@gmail.com");
            message.setTo("mentenseoul@gmail.com");
            message.setSubject("[TIMO.GG] 테스트 이메일");
            message.setText("안녕하세요! 이것은 테스트 이메일입니다.");

            JavaMailSender mailSender = emailTemplateService.getMailSender();
            mailSender.send(message);

            System.out.println("✅ 간단한 텍스트 이메일 전송 완료!");
            Thread.sleep(3000);

        } catch (Exception e) {
            System.err.println("❌ 텍스트 이메일 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}