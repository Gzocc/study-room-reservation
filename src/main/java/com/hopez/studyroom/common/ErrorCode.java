package com.hopez.studyroom.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "系统错误"),

    // 用户相关 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户名已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "账号已被禁用"),
    NOT_LOGIN(1005, "未登录"),
    NO_PERMISSION(1006, "无权限"),

    // 教室相关 2xxx
    ROOM_NOT_FOUND(2001, "教室不存在"),
    ROOM_CLOSED(2002, "教室已关闭"),

    // 座位相关 3xxx
    SEAT_NOT_FOUND(3001, "座位不存在"),
    SEAT_DISABLED(3002, "座位已禁用"),
    SEAT_OCCUPIED(3003, "座位已被预约"),

    // 预约相关 4xxx
    RESERVATION_NOT_FOUND(4001, "预约不存在"),
    RESERVATION_CANCELLED(4002, "预约已取消"),
    RESERVATION_EXPIRED(4003, "预约已过期"),
    RESERVATION_LIMIT(4004, "超过每日预约限制"),
    RESERVATION_TIME_INVALID(4005, "预约时间无效"),
    ALREADY_RESERVED(4006, "该时段已有预约"),

    // 签到相关 5xxx
    CHECKIN_TIMEOUT(5001, "签到超时"),
    ALREADY_CHECKIN(5002, "已签到"),
    ALREADY_CHECKOUT(5003, "已签退"),
    NOT_CHECKIN(5004, "未签到，无法签退"),

    // 违约相关 6xxx
    VIOLATION_LIMIT(6001, "违约次数已达上限"),
    ;

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
