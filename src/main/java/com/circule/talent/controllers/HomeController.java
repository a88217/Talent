package com.circule.talent.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Map<String, Object> model) {
        model.put("welcome", "Welcome to Talents!");
        return "home";
    }

}