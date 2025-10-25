package com.kakao_tech.community.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("lastModified", "2025.10.25");
        model.addAttribute("serviceName", "Community");
        model.addAttribute("officerName", "jayden");
        return "terms";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("lastModified", "2025.10.25");
        model.addAttribute("serviceName", "Community");
        model.addAttribute("officerName", "jayden");
        return "privacy";
    }
}
