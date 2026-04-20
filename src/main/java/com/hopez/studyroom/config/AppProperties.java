package com.hopez.studyroom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    /**
     * 签到提前时间（分钟）- 预约开始前多久可以签到
     */
    private Integer checkinEarly = 30;

    /**
     * 签到超时时间（分钟）- 预约开始后多久内可以签到
     */
    private Integer checkinTimeout = 60;

    /**
     * 最大违约次数
     */
    private Integer maxViolation = 5;

    /**
     * 单次预约最大时长（小时）
     */
    private Integer maxDuration = 4;

    /**
     * 提前预约天数
     */
    private Integer advanceDays = 3;
}
