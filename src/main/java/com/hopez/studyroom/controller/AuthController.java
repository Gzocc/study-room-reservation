package com.hopez.studyroom.controller;

import com.hopez.studyroom.common.Result;
import com.hopez.studyroom.entity.User;
import com.hopez.studyroom.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request.getUsername(), request.getPassword(),
                request.getRealName(), request.getRole());
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Result.success(token);
    }

    // 临时接口：快速创建超管（测试用）
    @GetMapping("/init-admin")
    public Result<?> initAdmin() {
        try {
            userService.register("admin", "123456", "超管", 2);
            return Result.success("超管创建成功，用户名：admin，密码：123456");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String realName;
        private Integer role;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
