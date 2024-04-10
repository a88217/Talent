package com.circule.talent.controllers;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.mapper.ClientMapper;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.model.Talent;
import com.circule.talent.repository.ClientRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.service.ClientService;
import com.circule.talent.service.TalentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class RegistrationController {

    private final TalentRepository talentRepository;

    private final ClientRepository clientRepository;

    private final TalentService talentService;

    private final ClientService clientService;

    @GetMapping("/talent_registration")
    public String talentRegistration() {
        return "talent_registration";
    }

    @RequestMapping(
            path = "/talent_registration",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createTalent(@Valid TalentCreateDTO talentData, Model model) {

        if (talentRepository.findByEmail(talentData.getEmail()).isPresent()) {
            model.addAttribute("message", "Пользователь с такой почтой уже зарегистрирован!");
            return "talent_registration";
        }

        talentService.create(talentData);

        return "redirect:/login";
    }

}
