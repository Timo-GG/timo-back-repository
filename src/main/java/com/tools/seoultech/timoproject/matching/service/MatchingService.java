package com.tools.seoultech.timoproject.matching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final BoardService boardService;
    private final UserService userService;


}
