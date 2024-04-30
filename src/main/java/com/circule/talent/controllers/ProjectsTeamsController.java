package com.circule.talent.controllers;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.repository.TeamRepository;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.ProjectService;
import com.circule.talent.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ProjectsTeamsController {

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final TeamService teamService;

    private final TalentRepository talentRepository;

    private final TeamRepository teamRepository;

    private final ProjectMapper projectMapper;

    private final ProfessionService professionService;


    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping(path = "/teams/{teamId}/project_build")
    public String buildTeamProject(@PathVariable Long teamId, Model model) {
        model.addAttribute("project", new ProjectCreateDTO());
        model.addAttribute("team_id", teamId);
        return "project_team_build";
    }


    @GetMapping(path = "/teams/{teamId}/projects/{projectId}")
    public String showTeamsProject(@PathVariable Long projectId, @PathVariable Long teamId, Model model) {

        var projectDTO = projectService.show(projectId);
        var teams = teamService.index();
        model.addAttribute("teams", teams);
        model.addAttribute("project", projectDTO);
        model.addAttribute("team_id", teamId);
        return "project";
    }

    @RequestMapping(
            path = "/teams/{teamId}/projects",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createTeamProject(@Valid @ModelAttribute("project") ProjectCreateDTO projectData, @PathVariable Long teamId, Model model, BindingResult bindingResult) {

        projectData.setPerformerId(teamId);
        if (bindingResult.hasErrors()) {
            return "project_team_build";
        }
        var project = projectRepository.findById(projectService.createTeamProject(projectData).getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        var team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
        team.addProject(project);
        teamRepository.save(team);
        model.addAttribute("team_id", teamId);
        return "redirect:/teams/" + teamId;
    }

    @PostMapping(path = "/teams/{teamId}/projects/{id}")
    public String updateTeamPhoto(@PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            project.setPhotoName(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        model.addAttribute("project", projectDTO);
        return "project";
    }

    @GetMapping(path = "/teams/{teamId}/projects/{id}/update")
    public String updateTeamProjectForm(@PathVariable Long teamId, @PathVariable Long id, Model model) {

        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));

        var projectDTO = projectMapper.map(project);

        model.addAttribute("project", projectDTO);
        model.addAttribute("team_id", teamId);
        model.addAttribute("project_id", id);
        return "project_team_update";
    }

    @RequestMapping(
            path = "/teams/{teamId}/projects/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String updateTeamProject(@Valid @ModelAttribute("user") ProjectCreateDTO projectData, @PathVariable Long teamId, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "project_team_update";
        }

        var projectUpdate = new ProjectUpdateDTO();

        projectUpdate.setTitle(JsonNullable.of(projectData.getTitle()));
        projectUpdate.setDescription(JsonNullable.of(projectData.getDescription()));

        projectService.update(projectUpdate, id);

        return "redirect:/teams/" + teamId;
    }

    @PostMapping(path = "/teams/{teamId}/projects/{id}/delete")
    public String deleteTeamProject(@PathVariable Long teamId, @PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/teams/" + teamId;
    }
}