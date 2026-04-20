package com.hopez.studyroom.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("seat")
public class Seat implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roomId;

    private String seatNo;

    private Integer rowNum;

    private Integer colNum;

    /**
     * 类型：0普通 1靠窗 2电源插座
     */
    private Integer type;

    /**
     * 状态：0禁用 1可用
     */
    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 当前预约状态：0空闲 1已预约 2使用中
     */
    @TableField(exist = false)
    private Integer reserveStatus;
}
