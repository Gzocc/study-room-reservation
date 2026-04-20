package com.hopez.studyroom.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
