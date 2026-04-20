package com.hopez.studyroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hopez.studyroom.entity.Seat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SeatService extends IService<Seat> {

    /**
     * 获取某教室的座位列表（带预约状态）
     */
    List<Seat> listSeatsByRoom(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * 修改座位状态
     */
    void changeStatus(Long seatId, Integer status);

    /**
     * 修改座位类型
     */
    void changeType(Long seatId, Integer type);
}
