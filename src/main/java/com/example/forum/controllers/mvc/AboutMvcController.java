package com.example.forum.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutMvcController {
    @GetMapping("/about")
    public String showAboutPage() {
        return "About";
    }
}
