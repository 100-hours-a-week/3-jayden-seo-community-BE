package com.kakao_tech.community.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class testController {

    @GetMapping("/origin")
    public void testOrigin(HttpServletRequest request) {
        log.info(request.getHeader("Origin"));
        log.info(request.getHeader("Host"));
        return;
    }
}
