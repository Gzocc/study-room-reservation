package com.hopez.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("checkin_record")
public class CheckinRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long reservationId;

    private Long userId;

    /**
     * 类型：1签到 2签退
     */
    private Integer type;

    private LocalDateTime time;

    private String ip;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
