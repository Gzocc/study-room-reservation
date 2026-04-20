package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.Reservation;
import com.hopez.studyroom.entity.Room;
import com.hopez.studyroom.entity.Seat;
import com.hopez.studyroom.entity.Violation;
import com.hopez.studyroom.mapper.ViolationMapper;
import com.hopez.studyroom.service.ReservationService;
import com.hopez.studyroom.service.RoomService;
import com.hopez.studyroom.service.SeatService;
import com.hopez.studyroom.util.AuthInterceptor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RoomService roomService;
    private final SeatService seatService;
    private final ReservationService reservationService;
    private final ViolationMapper violationMapper;
    private final AuthInterceptor authInterceptor;

    // ==================== 教室管理 ====================

    @PostMapping("/rooms")
    public Result<Room> addRoom(@RequestBody AddRoomRequest request) {
        Room room = new Room();
        room.setName(request.getName());
        room.setBuilding(request.getBuilding());
        room.setFloor(request.getFloor());
        room.setDescription(request.getDescription());
        room.setAdminId(authInterceptor.getUserId());
        room.setStatus(1);
        Room saved = roomService.addRoom(room, request.getRows(), request.getCols());
        return Result.success(saved);
    }

    @PutMapping("/rooms/{id}")
    public Result<?> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        room.setId(id);
        roomService.updateById(room);
        return Result.success();
    }

    @PutMapping("/rooms/{id}/status")
    public Result<?> changeRoomStatus(@PathVariable Long id, @RequestParam Integer status) {
        roomService.changeStatus(id, status);
        return Result.success();
    }

    @DeleteMapping("/rooms/{id}")
    public Result<?> deleteRoom(@PathVariable Long id) {
        roomService.removeById(id);
        return Result.success();
    }

    // ==================== 座位管理 ====================

    @GetMapping("/rooms/{roomId}/seats")
    public Result<List<Seat>> listSeats(@PathVariable Long roomId) {
        List<Seat> seats = seatService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Seat>()
                        .eq(Seat::getRoomId, roomId)
                        .orderByAsc(Seat::getRowNum)
                        .orderByAsc(Seat::getColNum));
        return Result.success(seats);
    }

    @PutMapping("/seats/{id}/status")
    public Result<?> changeSeatStatus(@PathVariable Long id, @RequestParam Integer status) {
        seatService.changeStatus(id, status);
        return Result.success();
    }

    @PutMapping("/seats/{id}/type")
    public Result<?> changeSeatType(@PathVariable Long id, @RequestParam Integer type) {
        seatService.changeType(id, type);
        return Result.success();
    }

    // ==================== 预约管理 ====================

    @GetMapping("/reservations")
    public Result<PageResult<Reservation>> listReservations(
            PageRequest pageRequest,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(reservationService.pageAllReservations(pageRequest, roomId, status, date));
    }

    // ==================== 违约记录 ====================

    @GetMapping("/violations")
    public Result<PageResult<Violation>> listViolations(PageRequest pageRequest) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Violation> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Violation>()
                        .orderByDesc(Violation::getCreateTime);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Violation> page =
                violationMapper.selectPage(
                        new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                                pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(),
                pageRequest.getPageNum(), pageRequest.getPageSize()));
    }

    // ==================== DTO ====================

    @Data
    public static class AddRoomRequest {
        private String name;
        private String building;
        private Integer floor;
        private String description;
        private Integer rows;
        private Integer cols;
    }
}
