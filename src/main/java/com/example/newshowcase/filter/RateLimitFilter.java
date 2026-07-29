package com.example.newshowcase.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 10_000;

    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<Long>> requestLog = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = getClientIp(request);
        long now = System.currentTimeMillis();

        ConcurrentLinkedDeque<Long> timestamps = requestLog.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>());

        timestamps.removeIf(ts -> now - ts > WINDOW_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests\",\"message\":\"More than 5 attempts from one IP-address during 10 seconds\"}");
            return;
        }

        timestamps.add(now);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"/auth/registration-email-resending".equals(request.getRequestURI())
                || !"POST".equalsIgnoreCase(request.getMethod());
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
