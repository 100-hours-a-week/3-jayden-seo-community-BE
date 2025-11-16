package com.kakao_tech.community.Config.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // ✅ Preflight (CORS 사전 요청) 무조건 허용
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        if(uri.startsWith("/auth/login") || uri.startsWith("/member/register")
                || uri.startsWith("/css") || uri.startsWith("/js")){
            return true;
        }

        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("memberId") == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json; charset=UTF-8"); // JSON 형식 지정

            String json = "{\"message\": \"로그인이 필요합니다.\"}";
            response.getWriter().write(json);
            return false;
        }

        return true;
    }
}