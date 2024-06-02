package com.circule.talent.controllers;

import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.model.User;
import com.circule.talent.service.MailSender;
import com.circule.talent.service.PackageService;
import com.circule.talent.service.TalentService;
import com.circule.talent.service.TeamService;
import com.circule.talent.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class HomeController {

    private final UserUtils userUtils;

    private final MailSender mailSender;

    private final PackageService packageService;

    private final TeamService teamService;

    private final TalentService talentService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public String homeBootstrap(TalentParamsDTO params, Model model) {
        var packages = packageService.index();
        var teams = teamService.index();
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("teams", teams);
        model.addAttribute("packages", packages);
        return "home_bootstrap";
    }

    @PostMapping("/home")
    public String sendFeedbackFromHomePage(@RequestParam String email,
                                           @RequestParam String name,
                                           @RequestParam String mobilePhone,
                                           @RequestParam String message, Model model) {

        String body = String.format("Запрос с сайта, \n" +
                "Имя: %s\n" +
                "Телефон: %s\n" +
                "Email: %s\n" +
                "Запрос: %s\n", name, mobilePhone, email, message);

        mailSender.send("muzalev.as@gmail.com", "Запрос с сайта Talents", body);

        model.addAttribute("message", "Письмо успешно отправлено, мы свяжемя с Вами в ближайшее время!");
        return "home_bootstrap";
    }

    @GetMapping("/for_talents")
    public String forTalents(TalentParamsDTO params, Model model) {
        var packages = packageService.index();
        var teams = teamService.index();
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("teams", teams);
        model.addAttribute("packages", packages);
        return "for_talents";
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        return "contact";
    }

    @PostMapping("/contact")
    public String sendFeedback(@RequestParam String email,
                               @RequestParam String name,
                               @RequestParam String mobilePhone,
                               @RequestParam String message, Model model) {

        String body = String.format("Запрос с сайта, \n" +
                "Имя: %s\n" +
                "Телефон: %s\n" +
                "Email: %s\n" +
                "Запрос: %s\n", name, mobilePhone, email, message);

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