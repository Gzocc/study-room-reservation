package com.hopez.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopez.studyroom.common.*;
import com.hopez.studyroom.entity.*;
import com.hopez.studyroom.mapper.*;
import com.hopez.studyroom.config.AppProperties;
import com.hopez.studyroom.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final SeatMapper seatMapper;
    private final RoomMapper roomMapper;
    private final UserMapper userMapper;
    private final CheckinRecordMapper checkinRecordMapper;
    private final ViolationMapper violationMapper;
    private final SystemConfigMapper systemConfigMapper;
    private final StringRedisTemplate redisTemplate;
    private final AppProperties appProperties;

    /**
     * 从数据库读取配置值，如果不存在则使用默认值
     */
    private int getConfigValue(String key, int defaultValue) {
        SystemConfig config = systemConfigMapper.selectOne(
            new LambdaQueryWrapper<SystemConfig>().eq(SystemConfig::getConfigKey, key)
        );
        if (config != null && config.getConfigValue() != null) {
            try {
                return Integer.parseInt(config.getConfigValue());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getCheckinEarly() {
        return getConfigValue("checkin_early", appProperties.getCheckinEarly());
    }

    private int getCheckinTimeout() {
        return getConfigValue("checkin_timeout", appProperties.getCheckinTimeout());
    }

    private int getMaxViolation() {
        return getConfigValue("max_violation_count", appProperties.getMaxViolation());
    }

    private int getMaxDuration() {
        return getConfigValue("reserve_duration", appProperties.getMaxDuration());
    }

    private int getAdvanceDays() {
        return getConfigValue("max_reserve_days", appProperties.getAdvanceDays());
    }

    @Override
    @Transactional
    public Reservation createReservation(Long userId, Long seatId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // 1. 校验用户状态
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }
        if (user.getViolationCount() >= getMaxViolation()) {
            throw new BusinessException(ErrorCode.VIOLATION_LIMIT);
        }

        // 2. 校验座位
        Seat seat = seatMapper.selectById(seatId);
        if (seat == null) {
            throw new BusinessException(ErrorCode.SEAT_NOT_FOUND);
        }
        if (seat.getStatus() == 0) {
            throw new BusinessException(ErrorCode.SEAT_DISABLED);
        }

        // 3. 校验教室
        Room room = roomMapper.selectById(seat.getRoomId());
        if (room == null || room.getStatus() == 0) {
            throw new BusinessException(ErrorCode.ROOM_CLOSED);
        }

        // 4. 校验时间
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_INVALID);
        }
        if (date.isAfter(LocalDate.now().plusDays(getAdvanceDays()))) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_INVALID);
        }
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_INVALID);
        }
        if (startTime.isBefore(LocalTime.of(8, 0)) || endTime.isAfter(LocalTime.of(22, 0))) {
            throw new BusinessException(ErrorCode.RESERVATION_TIME_INVALID);
        }

        // 5. 校验每日预约限制
        int dailyCount = reservationMapper.countUserReservationsOnDate(userId, date);
        if (dailyCount >= getMaxDuration()) {
            throw new BusinessException(ErrorCode.RESERVATION_LIMIT);
        }

        // 6. 校验用户该时段是否已有预约
        int userConflict = reservationMapper.countUserReservations(userId, date, startTime, endTime);
        if (userConflict > 0) {
            throw new BusinessException(ErrorCode.ALREADY_RESERVED);
        }

        // 7. 使用Redis分布式锁防止并发
        String lockKey = "seat_lock:" + seatId + ":" + date + ":" + startTime + "-" + endTime;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (locked == null || !locked) {
            throw new BusinessException(ErrorCode.SEAT_OCCUPIED);
        }

        try {
            // 8. 再次校验座位是否被预约
            int seatConflict = reservationMapper.countSeatReservations(seatId, date, startTime, endTime);
            if (seatConflict > 0) {
                throw new BusinessException(ErrorCode.SEAT_OCCUPIED);
            }

            // 9. 创建预约
            Reservation reservation = new Reservation();
            reservation.setUserId(userId);
            reservation.setRoomId(room.getId());
            reservation.setSeatId(seatId);
            reservation.setDate(date);
            reservation.setStartTime(startTime);
            reservation.setEndTime(endTime);
            reservation.setStatus(0);
            save(reservation);

            return reservation;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = getById(reservationId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        if (reservation.getStatus() != 0) {
            throw new BusinessException(ErrorCode.RESERVATION_CANCELLED);
        }

        reservation.setStatus(3);
        updateById(reservation);
    }

    @Override
    @Transactional
    public void checkin(Long reservationId, Long userId, String ip) {
        Reservation reservation = getById(reservationId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        if (reservation.getStatus() != 0) {
            throw new BusinessException(ErrorCode.ALREADY_CHECKIN);
        }

        // 校验签到时间（预约开始前N分钟到预约开始后M分钟内）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationStart = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
        LocalDateTime checkinStart = reservationStart.minusMinutes(getCheckinEarly());
        LocalDateTime checkinEnd = reservationStart.plusMinutes(getCheckinTimeout());

        if (now.isBefore(checkinStart)) {
            throw new BusinessException("签到时间未到");
        }
        if (now.isAfter(checkinEnd)) {
            // 超时，记违约
            handleViolation(reservation, userId, 1, "签到超时");
            throw new BusinessException(ErrorCode.CHECKIN_TIMEOUT);
        }

        // 签到
        reservation.setStatus(1);
        reservation.setCheckinTime(now);
        updateById(reservation);

        // 记录签到
        CheckinRecord record = new CheckinRecord();
        record.setReservationId(reservationId);
        record.setUserId(userId);
        record.setType(1);
        record.setTime(now);
        record.setIp(ip);
        checkinRecordMapper.insert(record);
    }

    @Override
    @Transactional
    public void checkout(Long reservationId, Long userId, String ip) {
        Reservation reservation = getById(reservationId);
        if (reservation == null) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        if (!reservation.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        if (reservation.getStatus() == 0) {
            throw new BusinessException(ErrorCode.NOT_CHECKIN);
        }
        if (reservation.getStatus() == 2) {
            throw new BusinessException(ErrorCode.ALREADY_CHECKOUT);
        }

        LocalDateTime now = LocalDateTime.now();

        // 签退
        reservation.setStatus(2);
        reservation.setCheckoutTime(now);
        updateById(reservation);

        // 记录签退
        CheckinRecord record = new CheckinRecord();
        record.setReservationId(reservationId);
        record.setUserId(userId);
        record.setType(2);
        record.setTime(now);
        record.setIp(ip);
        checkinRecordMapper.insert(record);
    }

    @Override
    public PageResult<Reservation> pageUserReservations(Long userId, Integer status, PageRequest pageRequest) {
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getUserId, userId);
        if (status != null) {
            wrapper.eq(Reservation::getStatus, status);
        }
        wrapper.orderByDesc(Reservation::getCreateTime);

        Page<Reservation> page = page(new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);
        fillReservationInfo(page.getRecords());

        return PageResult.of(page.getRecords(), page.getTotal(), pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public PageResult<Reservation> pageAllReservations(PageRequest pageRequest, Long roomId, Integer status, LocalDate date) {
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        if (roomId != null) {
            wrapper.eq(Reservation::getRoomId, roomId);
        }
        if (status != null) {
            wrapper.eq(Reservation::getStatus, status);
        }
        if (date != null) {
            wrapper.eq(Reservation::getDate, date);
        }
        wrapper.orderByDesc(Reservation::getCreateTime);

        Page<Reservation> page = page(new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);
        fillReservationInfo(page.getRecords());

        return PageResult.of(page.getRecords(), page.getTotal(), pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    @Transactional
    public void handleTimeoutReservations() {
        // 查询超时未签到的预约
        List<Reservation> timeoutList = list(new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getStatus, 0)
                .le(Reservation::getDate, LocalDate.now()));

        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : timeoutList) {
            LocalDateTime reservationStart = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
            LocalDateTime timeout = reservationStart.plusMinutes(getCheckinTimeout());

            if (now.isAfter(timeout)) {
                handleViolation(reservation, reservation.getUserId(), 1, "签到超时自动释放");
            }
        }
    }

    /**
     * 处理违约
     */
    private void handleViolation(Reservation reservation, Long userId, int reason, String description) {
        // 更新预约状态
        reservation.setStatus(4);
        updateById(reservation);

        // 记录违约
        Violation violation = new Violation();
        violation.setUserId(userId);
        violation.setReservationId(reservation.getId());
        violation.setReason(reason);
        violation.setDescription(description);
        violationMapper.insert(violation);

        // 增加用户违约次数
        User user = userMapper.selectById(userId);
        user.setViolationCount(user.getViolationCount() + 1);
        userMapper.updateById(user);

        // 检查是否达到上限
        if (user.getViolationCount() >= getMaxViolation()) {
            user.setStatus(0);
            userMapper.updateById(user);
        }
    }

    /**
     * 填充预约信息
     */
    private void fillReservationInfo(List<Reservation> list) {
        for (Reservation reservation : list) {
            User user = userMapper.selectById(reservation.getUserId());
            if (user != null) {
                reservation.setUserName(user.getRealName());
            }

            Room room = roomMapper.selectById(reservation.getRoomId());
            if (room != null) {
                reservation.setRoomName(room.getName());
            }

            Seat seat = seatMapper.selectById(reservation.getSeatId());
            if (seat != null) {
                reservation.setSeatNo(seat.getSeatNo());
            }
        }
    }
}
