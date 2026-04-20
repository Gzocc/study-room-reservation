package com.hopez.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("reservation")
public class Reservation implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long roomId;

    private Long seatId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    /**
     * 状态：0待签到 1已签到 2已签退 3已取消 4违约 5超时释放
     */
    private Integer status;

    private LocalDateTime checkinTime;

    private LocalDateTime checkoutTime;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String roomName;

    @TableField(exist = false)
    private String seatNo;
}
