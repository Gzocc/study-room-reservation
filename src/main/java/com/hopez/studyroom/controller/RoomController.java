package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.Room;
import com.hopez.studyroom.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public Result<List<Room>> listOpenRooms() {
        return Result.success(roomService.listOpenRooms());
    }

    @GetMapping("/{id}")
    public Result<Room> getRoom(@PathVariable Long id) {
        return Result.success(roomService.getById(id));
    }

    @GetMapping("/page")
    public Result<PageResult<Room>> pageRooms(PageRequest pageRequest,
                                              @RequestParam(required = false) String building,
                                              @RequestParam(required = false) Integer status) {
        return Result.success(roomService.pageRooms(pageRequest, building, status));
    }
}
