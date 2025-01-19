package com.tools.seoultech.timoproject.admin.controller;

import com.tools.seoultech.timoproject.admin.LoginRequired;
import com.tools.seoultech.timoproject.admin.service.AmemberService;
import com.tools.seoultech.timoproject.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/members")
@RequiredArgsConstructor
@LoginRequired
public class AmemberController {

    private final AmemberService memberService;

    // 회원 목록 첫 페이지로 리다이렉트
    @GetMapping
    public String index() {
        return "redirect:/admin/members/1";
    }

    // 특정 페이지의 회원 목록 보기
    @GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Member> page = memberService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "customers/list"; // customers/list.html 렌더링
    }

//    // 회원 추가 폼
//    @GetMapping("/add")
//    public String add(Model model) {
//        model.addAttribute("member", new Member());
//        return "customers/form"; // customers/form.html 렌더링
//    }

    // 회원 정보 수정 폼
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.get(id));
        return "customers/form"; // customers/form.html 렌더링
    }

    // 회원 저장
    @PostMapping(value = "/save")
    public String save(Member member, final RedirectAttributes ra) {
        memberService.save(member);
        ra.addFlashAttribute("successFlash", "회원 정보가 성공적으로 저장되었습니다.");
        return "redirect:/admin/members";
    }

    // 회원 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }
}
