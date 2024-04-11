package com.circule.talent.controllers;

import com.circule.talent.service.CustomUserDetailsService;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.TalentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/talents")
@AllArgsConstructor
public class TalentsController {

    private final TalentService talentService;

    private final CustomUserDetailsService userService;

    private final ProfessionService professionService;

    @GetMapping(path = "")
    public String index(Model model) {
        var talents = talentService.index();
        var professions = professionService.index();
        model.addAttribute("talents", talents);
        model.addAttribute("professions", professions);
        return "talents";
    }

    @GetMapping(path = "/{id}")
    public String show(@PathVariable Long id, Model model) {

        var talent = talentService.show(id);
        var professions = professionService.index();
        model.addAttribute("talent", talent);
        model.addAttribute("professions", professions);
        return "talent";
    }

}