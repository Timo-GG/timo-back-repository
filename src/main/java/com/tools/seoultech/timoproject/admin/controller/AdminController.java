package com.tools.seoultech.timoproject.admin.controller;

import com.tools.seoultech.timoproject.admin.AdminLog;
import com.tools.seoultech.timoproject.admin.AdminLogRepository;
import com.tools.seoultech.timoproject.admin.LoginRequired;
import com.tools.seoultech.timoproject.admin.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final AdminLogRepository adminLogRepository;

    @LoginRequired
    @GetMapping("")
    public String index(HttpSession session, Model model) {
        log.info("session: {}", session.getAttribute("isAdmin"));
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        List<AdminLog> logs = adminLogRepository.findAll();
        model.addAttribute("logs", logs);
        return "dashboard/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String adminLogin(@RequestParam(required = false) String error, org.springframework.ui.Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login"; // login.html 렌더링
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // AdminService를 사용하여 인증 처리
        if (adminService.authenticate(username, password)) {
            // 세션에 ADMIN 정보 저장
            session.setAttribute("isAdmin", true);
            return "redirect:/admin"; // 로그인 성공 시 대시보드로 이동
        } else {
            return "redirect:/admin/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

}
