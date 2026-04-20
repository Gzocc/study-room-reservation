package com.hopez.studyroom.service;

import com.hopez.studyroom.entity.OperationLog;
import com.hopez.studyroom.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class OperationLogService {

    private final OperationLogMapper operationLogMapper;

    public void log(Long userId, String module, String operation, String detail, String ip) {
        OperationLog log = new OperationLog();
        log.setUserId(userId != null ? userId : 0L);
        log.setModule(module);
        log.setOperation(operation);
        log.setDetail(detail);
        log.setIp(ip);
        operationLogMapper.insert(log);
    }

    public void log(Long userId, String module, String operation, String detail, HttpServletRequest request) {
        log(userId, module, operation, detail, getIpAddress(request));
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
