package com.hopez.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopez.studyroom.common.BusinessException;
import com.hopez.studyroom.common.ErrorCode;
import com.hopez.studyroom.entity.Seat;
import com.hopez.studyroom.mapper.ReservationMapper;
import com.hopez.studyroom.mapper.SeatMapper;
import com.hopez.studyroom.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements SeatService {

    private final ReservationMapper reservationMapper;

    @Override
    public List<Seat> listSeatsByRoom(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        LambdaQueryWrapper<Seat> wrapper = new LambdaQueryWrapper<Seat>()
                .eq(Seat::getRoomId, roomId)
                .eq(Seat::getStatus, 1)
                .orderByAsc(Seat::getRowNum)
                .orderByAsc(Seat::getColNum);

        List<Seat> seats = list(wrapper);

        // 设置每个座位的预约状态
        for (Seat seat : seats) {
            int count = reservationMapper.countSeatReservations(seat.getId(), date, startTime, endTime);
            if (count > 0) {
                seat.setReserveStatus(1); // 已预约
            } else {
                seat.setReserveStatus(0); // 空闲
            }
        }

        return seats;
    }

    @Override
    public void changeStatus(Long seatId, Integer status) {
        Seat seat = getById(seatId);
        if (seat == null) {
            throw new BusinessException(ErrorCode.SEAT_NOT_FOUND);
        }
        seat.setStatus(status);
        updateById(seat);
    }

    @Override
    public void changeType(Long seatId, Integer type) {
        Seat seat = getById(seatId);
        if (seat == null) {
            throw new BusinessException(ErrorCode.SEAT_NOT_FOUND);
        }
        seat.setType(type);
        updateById(seat);
    }
}
