package com.circule.talent.controllers;

import com.circule.talent.model.User;
import com.circule.talent.service.MailSender;
import com.circule.talent.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private MailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toSet());
        roles.stream().forEach(System.out::println);

        model.addAttribute("user", user);
        model.addAttribute("welcome", "Welcome to Talents!");
        return "home";
    }

    @GetMapping("/")
    public String mainPage() {
        return "home";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @PostMapping("/contact")
    public String sendFeedback(@RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String mobilPhone,
                               @RequestParam String message, Model model) {

        String body = String.format("Запрос с сайта, \n" +
                "Имя: %s\n" +
                "Телефон: %s\n" +
                "Email: %s\n" +
                "Запрос: %s\n", name, mobilPhone, email, message);

        mailSender.send("muzalev.as@gmail.com", "Запрос с сайта Talents", body);

        model.addAttribute("message", "Письмо успешно отправлено, мы свяжемя с Вами в ближайшее время!");
        return "contact";
    }

    @GetMapping("/about")
    public String about(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("welcome", "Welcome to Talents!");
        return "about";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        return "admin";
    }

}