package com.hopez.studyroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hopez.studyroom.entity.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalTime;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservation> {

    @Select("SELECT COUNT(*) FROM reservation WHERE user_id = #{userId} AND date = #{date} AND status IN (0, 1)")
    int countUserReservationsOnDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Select("SELECT COUNT(*) FROM reservation WHERE seat_id = #{seatId} AND date = #{date} " +
            "AND status IN (0, 1) AND ((start_time < #{endTime} AND end_time > #{startTime}))")
    int countSeatReservations(@Param("seatId") Long seatId, @Param("date") LocalDate date,
                              @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Select("SELECT COUNT(*) FROM reservation WHERE user_id = #{userId} AND date = #{date} " +
            "AND status IN (0, 1) AND ((start_time < #{endTime} AND end_time > #{startTime}))")
    int countUserReservations(@Param("userId") Long userId, @Param("date") LocalDate date,
                              @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
