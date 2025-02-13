package com.tools.seoultech.timoproject.admin.service;

import com.tools.seoultech.timoproject.admin.AdminLog;
import com.tools.seoultech.timoproject.admin.AdminLogRepository;
import com.tools.seoultech.timoproject.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcommentService {
    private final CommentRepository commentRepository;
    private final AdminLogRepository adminLogRepository;


    public void delete(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);

            //todo : 어드민 아이디 커스텀화 해야함
            AdminLog log = new AdminLog(AdminLog.EntityType.COMMENT, AdminLog.MethodType.DELETE, id.toString(), "admin");
            adminLogRepository.save(log);
        } else {
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. ID: " + id);
        }
    }
}
