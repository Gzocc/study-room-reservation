package com.hopez.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopez.studyroom.common.*;
import com.hopez.studyroom.entity.Room;
import com.hopez.studyroom.entity.Seat;
import com.hopez.studyroom.entity.User;
import com.hopez.studyroom.mapper.RoomMapper;
import com.hopez.studyroom.mapper.UserMapper;
import com.hopez.studyroom.service.RoomService;
import com.hopez.studyroom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    private final SeatService seatService;
    private final UserMapper userMapper;

    @Override
    public PageResult<Room> pageRooms(PageRequest pageRequest, String building, Integer status) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        if (building != null && !building.isEmpty()) {
            wrapper.eq(Room::getBuilding, building);
        }
        if (status != null) {
            wrapper.eq(Room::getStatus, status);
        }
        wrapper.orderByDesc(Room::getCreateTime);

        Page<Room> page = page(new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);

        // 填充管理员名称
        page.getRecords().forEach(room -> {
            if (room.getAdminId() != null) {
                User admin = userMapper.selectById(room.getAdminId());
                if (admin != null) {
                    room.setAdminName(admin.getRealName());
                }
            }
        });

        return PageResult.of(page.getRecords(), page.getTotal(), pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public List<Room> listOpenRooms() {
        return list(new LambdaQueryWrapper<Room>().eq(Room::getStatus, 1));
    }

    @Override
    @Transactional
    public Room addRoom(Room room, int rows, int cols) {
        save(room);

        // 批量生成座位
        int totalSeats = rows * cols;
        room.setTotalSeats(totalSeats);
        updateById(room);

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                Seat seat = new Seat();
                seat.setRoomId(room.getId());
                seat.setSeatNo(String.format("%c%02d", 'A' + r - 1, c));
                seat.setRowNum(r);
                seat.setColNum(c);
                seat.setType(0);
                seat.setStatus(1);
                seatService.save(seat);
            }
        }

        return room;
    }

    @Override
    public void changeStatus(Long roomId, Integer status) {
        Room room = getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        room.setStatus(status);
        updateById(room);
    }
}
