package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.PageRequest;
import com.hopez.studyroom.common.PageResult;
import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.OperationLog;
import com.hopez.studyroom.entity.SystemConfig;
import com.hopez.studyroom.entity.User;
import com.hopez.studyroom.mapper.OperationLogMapper;
import com.hopez.studyroom.mapper.SystemConfigMapper;
import com.hopez.studyroom.service.UserService;
import com.hopez.studyroom.service.OperationLogService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/super")
@RequiredArgsConstructor
public class SuperController {

    private final UserService userService;
    private final SystemConfigMapper systemConfigMapper;
    private final OperationLogMapper operationLogMapper;
    private final OperationLogService operationLogService;
    private final HttpServletRequest request;

    // ==================== 管理员管理 ====================

    @GetMapping("/admins")
    public Result<PageResult<User>> listAdmins(PageRequest pageRequest,
                                                @RequestParam(required = false) String keyword) {
        return Result.success(userService.pageUsers(pageRequest, 1, keyword));
    }

    @PostMapping("/admins")
    public Result<User> addAdmin(@RequestBody AddAdminRequest addRequest) {
        User user = userService.register(addRequest.getUsername(), addRequest.getPassword(),
                addRequest.getRealName(), 1);
        try {
            operationLogService.log(0L, "管理员管理", "添加管理员", 
                    "添加管理员：" + addRequest.getUsername(), request);
        } catch (Exception e) {
            // 日志记录失败不影响主操作
        }
        return Result.success(user);
    }

    @DeleteMapping("/admins/{id}")
    public Result<?> deleteAdmin(@PathVariable Long id) {
        User user = userService.getById(id);
        userService.removeById(id);
        try {
            operationLogService.log(0L, "管理员管理", "删除管理员", 
                    "删除管理员ID：" + id + "，用户名：" + (user != null ? user.getUsername() : ""), request);
        } catch (Exception e) {
            // 日志记录失败不影响主操作
        }
        return Result.success();
    }

    @PutMapping("/admins/{id}/status")
    public Result<?> changeAdminStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.changeStatus(id, status);
        return Result.success();
    }

    // ==================== 学生管理 ====================

    @GetMapping("/students")
    public Result<PageResult<User>> listStudents(PageRequest pageRequest,
                                                  @RequestParam(required = false) String keyword) {
        return Result.success(userService.pageUsers(pageRequest, 0, keyword));
    }

    @DeleteMapping("/students/{id}")
    public Result<?> deleteStudent(@PathVariable Long id) {
        User user = userService.getById(id);
        userService.removeById(id);
        try {
            operationLogService.log(0L, "学生管理", "删除学生", 
                    "删除学生ID：" + id + "，用户名：" + (user != null ? user.getUsername() : ""), request);
        } catch (Exception e) {
            // 日志记录失败不影响主操作
        }
        return Result.success();
    }

    @PutMapping("/students/{id}/status")
    public Result<?> changeStudentStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.changeStatus(id, status);
        try {
            operationLogService.log(0L, "学生管理", "修改状态", 
                    "修改学生ID：" + id + "，状态改为：" + (status == 1 ? "正常" : "禁用"), request);
        } catch (Exception e) {
            // 日志记录失败不影响主操作
        }
        return Result.success();
    }

    // ==================== 账号密码查询 ====================

    @GetMapping("/users/{id}/password")
    public Result<String> getUserPassword(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user.getPassword());
    }

    @PostMapping("/users/{id}/reset-password")
    public Result<?> resetPassword(@PathVariable Long id) {
        User user = userService.getById(id);
        userService.resetPassword(id, "123456");
        try {
            operationLogService.log(0L, "账号管理", "重置密码", 
                    "重置用户ID：" + id + "，用户名：" + (user != null ? user.getUsername() : "") + "，密码重置为：123456", request);
        } catch (Exception e) {
            // 日志记录失败不影响主操作
        }
        return Result.success("密码已重置为：123456");
    }

    // ==================== 系统配置 ====================

    @GetMapping("/system-config")
    public Result<java.util.List<SystemConfig>> listConfigs() {
        return Result.success(systemConfigMapper.selectList(null));
    }

    @PutMapping("/system-config")
    public Result<?> updateConfig(@RequestBody UpdateConfigRequest configRequest) {
        SystemConfig config = systemConfigMapper.selectById(configRequest.getId());
        if (config != null) {
            config.setConfigValue(configRequest.getConfigValue());
            systemConfigMapper.updateById(config);
        }
        return Result.success();
    }

    // ==================== 操作日志 ====================

    @GetMapping("/logs")
    public Result<PageResult<OperationLog>> listLogs(PageRequest pageRequest) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OperationLog> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OperationLog>()
                        .orderByDesc(OperationLog::getCreateTime);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<OperationLog> page =
                operationLogMapper.selectPage(
                        new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                                pageRequest.getPageNum(), pageRequest.getPageSize()), wrapper);
        return Result.success(PageResult.of(page.getRecords(), page.getTotal(),
                pageRequest.getPageNum(), pageRequest.getPageSize()));
    }

    // ==================== DTO ====================

    @Data
    public static class AddAdminRequest {
        private String username;
        private String password;
        private String realName;
    }

    @Data
    public static class UpdateConfigRequest {
        private Long id;
        private String configValue;
    }
}
