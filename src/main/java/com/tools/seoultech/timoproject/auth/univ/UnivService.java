package com.tools.seoultech.timoproject.auth.univ;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.auth.univ.entity.UnivVerification;
import com.tools.seoultech.timoproject.auth.univ.entity.University;
import com.tools.seoultech.timoproject.auth.univ.repository.UnivVerificationRepository;
import com.tools.seoultech.timoproject.auth.univ.repository.UniversityRepository;
import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.univcert.api.UnivCert;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class UnivService {
     @Value("${univ_api_key}")
     private String api_key;

     private final UniversityRepository universityRepository;
     private final UnivVerificationRepository univVerificationRepository;
     private final JavaMailSender mailSender;
     private final MemberService memberService;

     @Transactional(readOnly = true)
     public void checkUniv(String univName) {
          universityRepository.findByName(univName)
                  .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_UNIV));
     }

     @Transactional
     public Boolean certifyUniv(UnivRequestDTO requestDto) {
          University university = universityRepository.findByName(requestDto.univName())
                  .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_UNIV));

          if (!requestDto.univEmail().endsWith("@" + university.getDomain())) {
               throw new BusinessException(ErrorCode.MISMATCHED_EMAIL_DOMAIN);
          }

          if (memberService.isUnivEmailCertified(requestDto.univEmail())) {
               throw new BusinessException(ErrorCode.ALREADY_USED_UNIV_ACCOUNT);
          }

          String code = createVerificationCode();
          univVerificationRepository.findById(requestDto.univEmail())
                  .ifPresentOrElse(
                          verification -> verification.updateCode(code),
                          () -> univVerificationRepository.save(new UnivVerification(requestDto.univEmail(), requestDto.univName(), code))
                  );

          sendVerificationEmail(requestDto.univEmail(), code);
          return true;
     }

     @Transactional
     public Boolean verifyRequest(UnivRequestDTO requestDto, int code, Long memberId) {
          UnivVerification verification = univVerificationRepository.findById(requestDto.univEmail())
                  .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CERTIFY_REQUEST));

          if (LocalDateTime.now().isAfter(verification.getExpiresAt())) {
               throw new BusinessException(ErrorCode.EXPIRED_VERIFICATION_CODE);
          }

          if (!verification.getCode().equals(String.valueOf(code))) {
               throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
          }

          verification.verify();

          Member member = memberService.getById(memberId);

          CertifiedUnivInfo univInfo = new CertifiedUnivInfo(verification.getEmail(), verification.getUnivName());
          member.updateCertifiedUnivInfo(univInfo);

          return true;
     }

     @Transactional
     public void deleteCertifiedMember(Long memberId) {
          Member member = memberService.getById(memberId);
          member.clearCertifiedUnivInfo();
     }

     @Async("notificationTaskExecutor")
     public void sendVerificationEmail(String email, String code) {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setTo(email);
          message.setSubject("[Timo] 대학교 이메일 인증 코드입니다.");
          message.setText("인증 코드는 [" + code + "] 입니다. 10분 내에 입력해주세요.");
          mailSender.send(message);
     }

     private String createVerificationCode() {
          return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
     }

}
