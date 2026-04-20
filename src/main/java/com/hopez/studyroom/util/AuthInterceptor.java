package com.hopez.studyroom.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<String> usernameThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Integer> roleThreadLocal = new ThreadLocal<>();

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendError(response, "未登录");
            return false;
        }

        try {
            token = token.substring(7);
            Long userId = jwtUtil.getUserId(token);
            String username = jwtUtil.getUsername(token);
            Integer role = jwtUtil.getRole(token);

            userIdThreadLocal.set(userId);
            usernameThreadLocal.set(username);
            roleThreadLocal.set(role);
            return true;
        } catch (Exception e) {
            sendError(response, "token无效或已过期");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        userIdThreadLocal.remove();
        usernameThreadLocal.remove();
        roleThreadLocal.remove();
    }

    public Long getUserId() {
        return userIdThreadLocal.get();
    }

    public String getUsername() {
        return usernameThreadLocal.get();
    }

    public Integer getRole() {
        return roleThreadLocal.get();
    }

    private void sendError(HttpServletResponse response, String message) {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().write("{\"code\":401,\"message\":\"" + message + "\"}");
        } catch (Exception ignored) {
        }
    }
}
