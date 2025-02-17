package com.tools.seoultech.timoproject.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tools.seoultech.timoproject.global.config.TestSecurityConfig;
import com.tools.seoultech.timoproject.post.domain.dto.PostDTO;
import com.tools.seoultech.timoproject.post.service.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostApiController.class)
@ContextConfiguration(classes = PostApiController.class)
@Import(TestSecurityConfig.class)
class PostApiControllerTest {
    @MockBean
    private PostServiceImpl postService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testReadPost() throws Exception {
        Long postId = 1L;
        PostDTO.Response postDTO = PostDTO.Response.builder()
                .id(postId)
                .title("Title")
                .content("Content")
                .memberId(1L)
                .build();

        given(postService.read(postId)).willReturn(postDTO);

        mockMvc.perform(get("/api/v1/postApi/read/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Title"))
                .andExpect(jsonPath("$.data.content").value("Content"));

        then(postService).should().read(postId);
    }

    @Test
    void testGetAllPosts() throws Exception {
        PostDTO.Response postDTO1 = PostDTO.Response.builder()
                .id(1L)
                .title("Title1")
                .content("Content1")
                .memberId(1L)
                .build();

        PostDTO.Response postDTO2 = PostDTO.Response.builder()
                .id(2L)
                .title("Title2")
                .content("Content2")
                .memberId(2L)
                .build();

        List<PostDTO.Response> postDTOList = List.of(postDTO1, postDTO2);

        given(postService.readAll()).willReturn(postDTOList);

        mockMvc.perform(get("/api/v1/postApi/read/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("Title1"));

        then(postService).should().readAll();
    }

    @Test
    void testCreatePost() throws Exception {
        PostDTO.Request postDtoRequest = PostDTO.Request.builder()
                .title("New Post")
                .content("This is the content")
                .memberId(1L)
                .build();

        PostDTO.Response postDTO = PostDTO.Response.builder()
                .title("New Post")
                .content("This is the content")
                .memberId(2L)
                .build();

        given(postService.create(postDtoRequest)).willReturn(postDTO);

        mockMvc.perform(post("/api/v1/postApi/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value("New Post"))
                .andExpect(jsonPath("$.data.content").value("This is the content"));

        then(postService).should().create(postDtoRequest);
    }

    @Test
    void testUpdatePost() throws Exception {
        PostDTO.Response postDTO = PostDTO.Response.builder()
                .id(1L)
                .title("Updated Title")
                .content("Updated Content")
                .memberId(1L)
                .build();

        PostDTO.Request postDtoRequest = PostDTO.Request.builder()
                .title("Updated Title")
                .content("Updated Content")
                .memberId(1L)
                .build();

        given(postService.update(1L, postDtoRequest)).willReturn(postDTO);

        mockMvc.perform(put("/api/v1/postApi/update/%d".formatted(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.content").value("Updated Content"));

        then(postService).should().update(1L, postDtoRequest);
    }

    @Test
    void testDeletePost() throws Exception {
        Long postId = 1L;

        BDDMockito.willDoNothing().given(postService).delete(postId);

        mockMvc.perform(delete("/api/v1/postApi/delete/{id}", postId))
                .andExpect(status().isOk());

        then(postService).should().delete(postId);
    }
}
