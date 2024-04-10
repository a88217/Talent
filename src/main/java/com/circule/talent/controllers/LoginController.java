package com.circule.talent.controllers;

import com.circule.talent.dto.AuthRequest;
import com.circule.talent.repository.UserRepository;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.TalentService;
import com.circule.talent.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String talentRegistration() {
        return "login";
    }

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TalentService talentService;

    @Autowired
    private ProfessionService professionService;

    @RequestMapping(
            path = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String create(AuthRequest authRequest, Model model) {
        System.out.println("Username: " + authRequest.getUsername());
        System.out.println("Password: " + authRequest.getPassword());
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());
        authenticationManager.authenticate(authentication);
        var token = jwtUtils.generateToken(authRequest.getUsername());
        var userId = userRepository.findByEmail(authRequest.getUsername()).get().getId();
        var talent = talentService.show(userId);
        var professions = professionService.index();
        model.addAttribute("talent", talent);
        model.addAttribute("professions", professions);
        return "redirect:/talents/" + userId;
    }
}
