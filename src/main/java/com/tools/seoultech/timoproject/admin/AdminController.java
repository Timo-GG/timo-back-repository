package com.tools.seoultech.timoproject.admin;

import com.tools.seoultech.timoproject.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("")
    public String index() {
        return "dashboard/index"; // main page
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "login"; // login.html 렌더링
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password) {
        // AdminService를 사용하여 인증 처리
        if (adminService.authenticate(username, password)) {
            return "redirect:/admin"; // 로그인 성공 시 대시보드로 이동
        } else {
            return "redirect:/admin/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
        }
    }

}
