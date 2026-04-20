package com.hopez.studyroom.task;

import com.hopez.studyroom.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务 - 处理超时预约
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationTask {

    private final ReservationService reservationService;

    /**
     * 每5分钟检查一次超时未签到的预约
     * 自动标记违约并释放座位
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)  // 5分钟
    public void handleTimeoutReservations() {
        log.info("开始处理超时预约...");
        try {
            reservationService.handleTimeoutReservations();
            log.info("超时预约处理完成");
        } catch (Exception e) {
            log.error("处理超时预约失败", e);
        }
    }
}
