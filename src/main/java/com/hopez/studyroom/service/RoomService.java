package com.hopez.studyroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.entity.Room;

import java.util.List;

public interface RoomService extends IService<Room> {

    /**
     * 分页查询教室
     */
    PageResult<Room> pageRooms(PageRequest pageRequest, String building, Integer status);

    /**
     * 获取所有开放的教室
     */
    List<Room> listOpenRooms();

    /**
     * 添加教室（同时批量生成座位）
     */
    Room addRoom(Room room, int rows, int cols);

    /**
     * 修改教室状态
     */
    void changeStatus(Long roomId, Integer status);
}
