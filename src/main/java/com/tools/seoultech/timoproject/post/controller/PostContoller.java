package com.tools.seoultech.timoproject.post.controller;

import com.tools.seoultech.timoproject.post.domain.dto.PageDTO;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Log4j2
public class PostContoller {
    private final PostService postService;

    @GetMapping("")
    public String showList(){
        log.info("Redirect:/framer/posts/fragment");
        return "redirect:/framer/posts/fragment";
    }
    @GetMapping("fragment")
    public void showList(PageDTO.Request dto, Model model){
        log.info("list................"+dto);
        model.addAttribute("result", postService.getList(dto));
    }
    @GetMapping("/read")
    public void read(long id, @ModelAttribute("requestDTO") PageDTO.Request requestDTO, Model model){
        log.info("id: "+id);
        PostDTO dto = postService.read(id);
        model.addAttribute("dto", dto);
    }
}
