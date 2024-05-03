package com.circule.talent.controllers;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.model.User;
import com.circule.talent.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class LoginController {

    private final UserUtils userUtils;

    @GetMapping("/login")
    public String talentRegistration(Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("userDTO", new TalentCreateDTO());
        model.addAttribute("user", user);
        return "login";
    }

}
