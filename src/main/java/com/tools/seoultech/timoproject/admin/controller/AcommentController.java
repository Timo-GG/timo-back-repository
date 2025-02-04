package com.tools.seoultech.timoproject.admin.controller;

import com.tools.seoultech.timoproject.admin.LoginRequired;
import com.tools.seoultech.timoproject.admin.service.AcommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@LoginRequired
public class AcommentController {

    private final AcommentService commentService;

    @GetMapping("/delete/{postId}/{id}")
    public String delete(@PathVariable Long postId, @PathVariable Long id) {
        commentService.delete(id);
        return "redirect:/admin/posts/edit/" + postId;
    }
}
