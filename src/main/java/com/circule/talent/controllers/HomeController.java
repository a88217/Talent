package com.circule.talent.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("welcome", "Welcome to Talents!");
        return "home";
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("welcome", "Welcome to Talents!");
        return "home";
    }

    @GetMapping("/about")
    public String about() {

        return "about";
    }

}