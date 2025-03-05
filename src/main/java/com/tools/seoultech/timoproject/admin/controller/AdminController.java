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
@RequestMapping("/admin/v1")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final AdminLogRepository adminLogRepository;

    @LoginRequired
    @GetMapping("")
    public String index(HttpSession session, Model model) {
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/v1/login";
        }
        List<AdminLog> logs = adminLogRepository.findAll();
        model.addAttribute("logs", logs);
        return "thymeleaf/dashboard/index"; // Thymeleaf 템플릿: dashboard/index.html
    }

    @GetMapping("/login")
    public String adminLogin(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }

        return "thymeleaf/login"; // 관리자 전용 로그인 페이지 템플릿: login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String nickname, @RequestParam String password, HttpSession session) {
        log.info("🔵 [LOGIN REQUEST] Received login attempt - nickname: {}, password: {}", nickname, password);

        if (adminService.authenticate(nickname, password)) {
            log.info("🟢 [LOGIN SUCCESS] Admin logged in successfully: {}", nickname);
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/v1";
        } else {
            log.warn("🔴 [LOGIN FAILED] Invalid credentials for nickname: {}", nickname);
            return "redirect:/admin/v1/login?error=true";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/v1/login";
    }
}
