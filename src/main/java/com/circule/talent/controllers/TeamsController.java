package com.circule.talent.controllers;

import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.dto.teams.TeamCreateDTO;
import com.circule.talent.dto.teams.TeamUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.TeamMapper;
import com.circule.talent.model.User;
import com.circule.talent.repository.TeamRepository;
import com.circule.talent.service.PackageService;
import com.circule.talent.service.ProjectService;
import com.circule.talent.service.TalentService;
import com.circule.talent.service.TeamService;
import com.circule.talent.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamsController {

    private final PackageService packageService;

    private final TeamService teamService;

    private final TalentService talentService;

    private final ProjectService projectService;

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final UserUtils userUtils;

    @GetMapping(path = "")
    public String index(TalentParamsDTO params, Model model) {
        var packages = packageService.index();
        var teams = teamService.index();
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("packages", packages);
        model.addAttribute("teams", teams);
        model.addAttribute("talents", talents);
        return "teams";
    }

    @GetMapping(path = "/{id}")
    public String show(@PathVariable Long id, TalentParamsDTO params, Model model) {

        var teamDTO = teamService.show(id);
        var talents = talentService.index(params);
        var projects = projectService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("team", teamDTO);
        model.addAttribute("talents", talents);
        model.addAttribute("projects", projects);
        return "team";
    }

    @GetMapping(path = "/build")
    public String buildTeam(TalentParamsDTO params, Model model) {

        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);

        model.addAttribute("talents", talents);
        model.addAttribute("team", new TeamCreateDTO());
        return "team_build";
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createTeam(@Valid @ModelAttribute("team") TeamCreateDTO teamData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "team_build";
        }

        teamService.create(teamData);
        return "redirect:/teams";
    }

    @GetMapping(path = "/update/{id}")
    public String updateTeamForm(@PathVariable Long id, TalentParamsDTO params, Model model) {

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + id + " not found"));
        var teamDTO = teamMapper.map(team);
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("team", teamDTO);
        model.addAttribute("talents", talents);
        return "team_update";
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String updateTeam(@Valid @ModelAttribute("team") TeamCreateDTO teamData, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "team_update";
        }

        var teamUpdate = new TeamUpdateDTO();

        teamUpdate.setTitle(JsonNullable.of(teamData.getTitle()));
        teamUpdate.setDescription(JsonNullable.of(teamData.getDescription()));
        teamUpdate.setTalentIds(JsonNullable.of(teamData.getTalentIds()));

        teamService.update(teamUpdate, id);

        return "redirect:/teams/" + id;
    }

}
