package com.circule.talent.controllers;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.repository.ClientRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.repository.UserRepository;
import com.circule.talent.service.ClientService;
import com.circule.talent.service.TalentService;
import com.circule.talent.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final TalentRepository talentRepository;

    private final ClientRepository clientRepository;

    private final TalentService talentService;

    private final ClientService clientService;

    private final UserRepository userRepository;

    private final UserUtils userUtils;

    private PasswordEncoder encoder;

    @GetMapping("/talent_registration")
    public String talentRegistration(Model model) {
        model.addAttribute("user", new TalentCreateDTO());
        return "talent_registration";
    }

    @GetMapping("/client_registration")
    public String clientRegistration(Model model) {
        model.addAttribute("user", new ClientCreateDTO());
        return "client_registration";
    }

    @RequestMapping(
            path = "/talent_registration",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createTalent(@Valid @ModelAttribute("user") TalentCreateDTO talentData, Model model, BindingResult bindingResult) {

        if (!talentData.getPassword().equals(talentData.getPasswordConfirmation())) {
            model.addAttribute("passwordIncorrect", "Пароли не совпадают");
            return "talent_registration";
        }

        if (bindingResult.hasErrors()) {
            return "talent_registration";
        }

        if (talentRepository.findByEmail(talentData.getEmail()).isPresent() || clientRepository.findByEmail(talentData.getEmail()).isPresent() ) {
            bindingResult.rejectValue("email", "", "Пользователь с почтой " + talentData.getEmail() + " уже зарегистрирован");
            return "talent_registration";
        }

        talentService.create(talentData);

        return "redirect:/login";
    }

    @RequestMapping(
            path = "/client_registration",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createClient(@Valid @ModelAttribute("user") ClientCreateDTO clientData, Model model, BindingResult bindingResult) {

        if (!clientData.getPassword().equals(clientData.getPasswordConfirmation())) {
            model.addAttribute("passwordIncorrect", "Пароли не совпадают");
            return "client_registration";
        }

        if (bindingResult.hasErrors()) {
            return "client_registration";
        }

        if (clientRepository.findByEmail(clientData.getEmail()).isPresent() || talentRepository.findByEmail(clientData.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "", "Пользователь с почтой " + clientData.getEmail() + " уже зарегистрирован");
            return "client_registration";
        }

        clientService.create(clientData);

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registration() {
        return "registration";
    }

    @GetMapping("/users/{id}/change_password")
    public String changePasswordForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "change_password";
    }

    @PostMapping("/users/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public String changeUserPassword(@PathVariable Long id, Model model,
                                              @RequestParam("password") String password,
                                              @RequestParam("oldPassword") String oldPassword,
                                              @RequestParam("passwordConfirmation") String passwordConfirmation) {

        if (!password.equals(passwordConfirmation)) {
            System.out.println("Не совпадают новые пароли");
            model.addAttribute("userId", id);
            model.addAttribute("passwordMismatch", "Пароли не совпадают");
            return "change_password";
        }

        var user = userUtils.getCurrentUser();

        if (!encoder.matches(oldPassword, user.getPassword())) {
            System.out.println("Не совпадает старый пароль");
            model.addAttribute("userId", id);
            model.addAttribute("wrongOldPassword", "Старый пароль введён неверно!");
            return "change_password";
        }

        user.setPasswordDigest(encoder.encode(password));
        userRepository.save(user);

        return "redirect:/home";
    }

}
