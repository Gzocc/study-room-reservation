package com.hopez.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("room")
public class Room implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String building;

    private Integer floor;

    private Integer totalSeats;

    private String description;

    /**
     * 状态：0关闭 1开放
     */
    private Integer status;

    private Long adminId;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String adminName;
}
