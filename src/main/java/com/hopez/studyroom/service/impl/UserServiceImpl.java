package com.hopez.studyroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopez.studyroom.common.*;
import com.hopez.studyroom.entity.User;
import com.hopez.studyroom.mapper.UserMapper;
import com.hopez.studyroom.service.UserService;
import com.hopez.studyroom.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(String username, String password, String realName, Integer role) {
        // 检查用户名是否已存在
        User existing = getByUsername(username);
        if (existing != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRealName(realName);
        user.setRole(role);
        user.setStatus(1);
        user.setViolationCount(0);

        save(user);
        user.setPassword(null); // 不返回密码
        return user;
    }

    @Override
    public String login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 临时：支持明文密码（调试用）
        boolean matches = passwordEncoder.matches(password, user.getPassword()) 
                || password.equals(user.getPassword());
        if (!matches) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRealName(), user.getRole());
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public PageResult<User> pageUsers(PageRequest pageRequest, Integer role, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (role != null) {
            wrapper.eq(User::getRole, role);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getRealName, keyword));
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> page = page(new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);
        page.getRecords().forEach(u -> u.setPassword(null));

        return PageResult.of(page.getRecords(), page.getTotal(), pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public void changeStatus(Long userId, Integer status) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setStatus(status);
        updateById(user);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }
}
