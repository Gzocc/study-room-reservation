package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.Reservation;
import com.hopez.studyroom.service.ReservationService;
import com.hopez.studyroom.util.AuthInterceptor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final AuthInterceptor authInterceptor;

    @PostMapping
    public Result<Reservation> create(@RequestBody CreateRequest request) {
        Long userId = authInterceptor.getUserId();
        Reservation reservation = reservationService.createReservation(
                userId, request.getSeatId(), request.getDate(),
                request.getStartTime(), request.getEndTime());
        return Result.success(reservation);
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        Long userId = authInterceptor.getUserId();
        reservationService.cancelReservation(id, userId);
        return Result.success();
    }

    @PostMapping("/{id}/checkin")
    public Result<?> checkin(@PathVariable Long id, @RequestParam String ip) {
        Long userId = authInterceptor.getUserId();
        reservationService.checkin(id, userId, ip);
        return Result.success();
    }

    @PostMapping("/{id}/checkout")
    public Result<?> checkout(@PathVariable Long id, @RequestParam String ip) {
        Long userId = authInterceptor.getUserId();
        reservationService.checkout(id, userId, ip);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<PageResult<Reservation>> myReservations(@RequestParam(required = false) Integer status,
                                                           PageRequest pageRequest) {
        Long userId = authInterceptor.getUserId();
        return Result.success(reservationService.pageUserReservations(userId, status, pageRequest));
    }

    @Data
    public static class CreateRequest {
        private Long seatId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;
    }
}
