package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.Seat;
import com.hopez.studyroom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/room/{roomId}")
    public Result<List<Seat>> listSeats(@PathVariable Long roomId,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                        @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                        @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime) {
        return Result.success(seatService.listSeatsByRoom(roomId, date, startTime, endTime));
    }
}
