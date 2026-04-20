package com.hopez.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("room_open_time")
public class RoomOpenTime implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roomId;

    /**
     * 星期几：1-7
     */
    private Integer dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
