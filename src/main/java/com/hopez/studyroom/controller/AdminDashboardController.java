package com.hopez.studyroom.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.Reservation;
import com.hopez.studyroom.entity.Room;
import com.hopez.studyroom.entity.Seat;
import com.hopez.studyroom.mapper.ReservationMapper;
import com.hopez.studyroom.mapper.RoomMapper;
import com.hopez.studyroom.mapper.SeatMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final RoomMapper roomMapper;
    private final SeatMapper seatMapper;
    private final ReservationMapper reservationMapper;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard() {
        Map<String, Object> data = new HashMap<>();

        // 教室总数
        Long roomCount = roomMapper.selectCount(null);
        data.put("roomCount", roomCount);

        // 座位总数
        Long seatCount = seatMapper.selectCount(null);
        data.put("seatCount", seatCount);

        // 今日预约数
        LocalDate today = LocalDate.now();
        Long todayReservations = reservationMapper.selectCount(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getDate, today)
                        .in(Reservation::getStatus, 0, 1)
        );
        data.put("todayReservations", todayReservations);

        // 使用率计算 - 当前正在使用的座位数（已签到状态）
        Long inUseCount = reservationMapper.selectCount(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getDate, today)
                        .eq(Reservation::getStatus, 1)  // 状态1 = 已签到（正在使用中）
        );
        data.put("inUseCount", inUseCount != null ? inUseCount : 0);
        
        long totalSeats = seatCount != null ? seatCount : 0;
        long usedSeats = inUseCount != null ? inUseCount : 0;
        int usageRate = totalSeats > 0 ? (int) (usedSeats * 100 / totalSeats) : 0;
        data.put("usageRate", Math.min(usageRate, 100));

        return Result.success(data);
    }
}
