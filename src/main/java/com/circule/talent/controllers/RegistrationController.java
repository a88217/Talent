package com.circule.talent.controllers;

import com.circule.talent.dto.clients.ClientCreateDTO;
import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.repository.ClientRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.service.ClientService;
import com.circule.talent.service.TalentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final TalentRepository talentRepository;

    private final ClientRepository clientRepository;

    private final TalentService talentService;

    private final ClientService clientService;

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
    public String createTalent(@Valid @ModelAttribute("user") TalentCreateDTO talentData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "talent_registration";
        }

        if (talentRepository.findByEmail(talentData.getEmail()).isPresent()) {
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
    public String createClient(@Valid @ModelAttribute("user") ClientCreateDTO clientData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "client_registration";
        }

        if (clientRepository.findByEmail(clientData.getEmail()).isPresent()) {
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

}
