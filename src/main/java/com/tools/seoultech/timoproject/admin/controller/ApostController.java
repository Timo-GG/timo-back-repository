package com.tools.seoultech.timoproject.admin.controller;

import com.tools.seoultech.timoproject.admin.LoginRequired;
import com.tools.seoultech.timoproject.admin.service.ApostService;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
@LoginRequired
public class ApostController {

    private final ApostService postService;

    // 게시글 목록 첫 페이지로 리다이렉트
    @GetMapping
    public String index() {
        return "redirect:/admin/posts/1";
    }

    // 특정 페이지의 게시글 목록 보기
    @GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
        Page<Post> page = postService.getList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "posts/list"; // posts/list.html 렌더링
    }

    // 게시글 작성 폼
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form"; // posts/form.html 렌더링
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Post post = postService.get(id);
        System.out.println("Post loaded for edit: " + post);
        model.addAttribute("post", post);
        return "posts/form";
    }

    // 게시글 저장
    @PostMapping("/save")
    public String save(@ModelAttribute Post post, RedirectAttributes ra) {
        try {
            postService.save(post); // 게시글 저장 로직 호출
            ra.addFlashAttribute("successFlash", "게시글이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorFlash", "게시글 저장 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace(); // 오류 로그 출력
        }
        return "redirect:/admin/posts";
    }



    // 게시글 삭제
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/admin/posts";
    }
}
