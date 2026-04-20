package com.hopez.studyroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationService extends IService<Reservation> {

    /**
     * 创建预约
     */
    Reservation createReservation(Long userId, Long seatId, LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * 取消预约
     */
    void cancelReservation(Long reservationId, Long userId);

    /**
     * 签到
     */
    void checkin(Long reservationId, Long userId, String ip);

    /**
     * 签退
     */
    void checkout(Long reservationId, Long userId, String ip);

    /**
     * 查询用户的预约记录
     */
    PageResult<Reservation> pageUserReservations(Long userId, Integer status, PageRequest pageRequest);

    /**
     * 查询所有预约记录（管理员）
     */
    PageResult<Reservation> pageAllReservations(PageRequest pageRequest, Long roomId, Integer status, LocalDate date);

    /**
     * 处理超时未签到的预约（定时任务调用）
     */
    void handleTimeoutReservations();
}
