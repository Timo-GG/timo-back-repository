package com.tools.seoultech.timoproject.riot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
class RealEnvDeadlockTest {

    private static final int POOL_SIZE = 10;
    private static final int NUM_CHILD_TASKS = 10;

    private ThreadPoolTaskExecutor testExecutor;

    @BeforeEach
    void setUp() {
        testExecutor = new ThreadPoolTaskExecutor();
        // 상수를 사용하여 스레드 풀 설정
        testExecutor.setCorePoolSize(POOL_SIZE);
        testExecutor.setMaxPoolSize(POOL_SIZE);
        testExecutor.setThreadNamePrefix("RealTest-");
        testExecutor.initialize();
    }

    @Test
    @DisplayName("스레드 풀 환경에서, 부모 작업이 풀 크기 이상의 자식 작업을 호출하면 교착상태가 발생한다")
    @org.junit.jupiter.api.Timeout(value = 5, unit = TimeUnit.SECONDS)
    void parent_with_enough_children_causes_deadlock() {
        log.info("테스트 시작: 스레드 풀 크기 = {}", POOL_SIZE);

        // 부모 작업 1개 + 자식 작업 (POOL_SIZE 개) = 총 POOL_SIZE + 1개의 스레드가 필요
        CompletableFuture<Void> parentTask = CompletableFuture.runAsync(() -> {
            log.info("부모 작업 시작 on {}", Thread.currentThread().getName());

            List<CompletableFuture<Void>> childTasks = IntStream.rangeClosed(1, NUM_CHILD_TASKS)
                    .mapToObj(i -> CompletableFuture.runAsync(() -> {
                        log.info("자식 작업-{} 시작 on {}", i, Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000); // 2초 작업
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }, testExecutor))
                    .toList();

            log.info("부모 작업이 {}명의 자식들을 기다립니다... (스레드 점유 중)", NUM_CHILD_TASKS);
            CompletableFuture.allOf(childTasks.toArray(new CompletableFuture[0])).join();
            log.info("부모 작업 완료! (이 로그는 찍히면 안 됨)");

        }, testExecutor);

        try {
            parentTask.get(4, TimeUnit.SECONDS);
            fail("교착상태가 발생하지 않고 테스트가 정상 종료됨");
        } catch (Exception e) {
            log.error("의도대로 Timeout 또는 다른 예외 발생 성공!: {}", e.getMessage());
        }
    }
}