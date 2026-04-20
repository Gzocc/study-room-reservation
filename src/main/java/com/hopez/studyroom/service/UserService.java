package com.hopez.studyroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.entity.User;

public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(String username, String password, String realName, Integer role);

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据用户名查询
     */
    User getByUsername(String username);

    /**
     * 分页查询用户
     */
    PageResult<User> pageUsers(PageRequest pageRequest, Integer role, String keyword);

    /**
     * 修改用户状态
     */
    void changeStatus(Long userId, Integer status);

    /**
     * 重置密码
     */
    void resetPassword(Long userId, String newPassword);
}
