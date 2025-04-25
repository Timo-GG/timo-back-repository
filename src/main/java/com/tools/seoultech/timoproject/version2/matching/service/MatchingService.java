package com.tools.seoultech.timoproject.version2.matching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final BoardService boardService;
    private final UserService userService;


}
