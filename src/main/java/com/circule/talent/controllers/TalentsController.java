package com.circule.talent.controllers;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.TalentMapper;
import com.circule.talent.model.User;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.ProjectService;
import com.circule.talent.service.TalentService;
import com.circule.talent.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/talents")
@RequiredArgsConstructor
public class TalentsController {

    private final TalentService talentService;

    private final TalentRepository talentRepository;

    private final ProjectService projectService;

    private final ProfessionService professionService;

    private final TalentMapper talentMapper;

    private final UserUtils userUtils;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping(path = "")
    public String index(TalentParamsDTO params, Model model) {
        var talents = talentService.index(params);
        var professions = professionService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("professions", professions);
        return "talents";
    }

    @GetMapping(path = "/{id}")
    public String show(@PathVariable Long id, Model model) {

        var talentDTO = talentService.show(id);
        var professions = professionService.index();
        var projects = projectService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talent", talentDTO);
        model.addAttribute("professions", professions);
        model.addAttribute("projects", projects);
        return "talent";
    }

    @GetMapping(path = "/{id}/profile")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public String showProfile(@PathVariable Long id, Model model) {

        var talentDTO = talentService.show(id);
        var professions = professionService.index();
        var projects = projectService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talent", talentDTO);
        model.addAttribute("professions", professions);
        model.addAttribute("projects", projects);
        return "talent_profile";
    }

    @PostMapping(path = "/{id}/profile")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public String updatePhoto(@PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        var talent = talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));
        var professions = professionService.index();
        var projects = projectService.index();
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            talent.setPhotoName(resultFilename);
            talentRepository.save(talent);
        }
        var talentDTO = talentService.show(id);
        model.addAttribute("talent", talentDTO);
        model.addAttribute("professions", professions);
        model.addAttribute("projects", projects);
        return "talent_profile";
    }

    @RequestMapping(
            path = "/update/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public String updateTalent(@Valid @ModelAttribute("user") TalentCreateDTO talentData, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "talent_update";
        }


        var talentUpdate = new TalentUpdateDTO();

        talentUpdate.setFirstName(JsonNullable.of(talentData.getFirstName()));
        talentUpdate.setLastName(JsonNullable.of(talentData.getLastName()));
        talentUpdate.setAbout(JsonNullable.of(talentData.getAbout()));
        talentUpdate.setMobilePhone(JsonNullable.of(talentData.getMobilePhone()));
        talentUpdate.setProfessionIds(JsonNullable.of(talentData.getProfessionIds()));

        talentService.update(talentUpdate, id);

        return "redirect:/talents/" + id + "/profile";
    }

    @GetMapping(path = "/update/{id}")
    @PreAuthorize("@userUtils.isCurrentUser(#id)")
    public String updateTalentForm(@PathVariable Long id, Model model) {

        var talent = talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));

        var professions = professionService.index();

        var talentDTO = talentMapper.map(talent);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);

        model.addAttribute("user", talentDTO);
        model.addAttribute("talent_id", id);
        model.addAttribute("professions", professions);
        return "talent_update";
    }

}