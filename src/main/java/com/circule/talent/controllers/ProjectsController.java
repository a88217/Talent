package com.circule.talent.controllers;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.ProjectService;
import com.circule.talent.service.TalentService;
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
public class ProjectsController {

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final TalentService talentService;

    private final TalentRepository talentRepository;

    private final ProjectMapper projectMapper;

    private final ProfessionService professionService;


    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping(path = "/projects")
    public String index(TalentParamsDTO params, Model model) {
        var talents = talentService.index(params);
        var projects = projectService.index();
        model.addAttribute("talents", talents);
        model.addAttribute("professions", projects);
        return "projects";
    }

    @GetMapping(path = "/projects/{projectId}")
    public String show(TalentParamsDTO params, @PathVariable Long projectId, Model model) {

        var projectDTO = projectService.show(projectId);
        var talents = talentService.index(params);
        model.addAttribute("talents", talents);
        model.addAttribute("project", projectDTO);
        return "project";
    }

    @GetMapping(path = "/talents/{talentId}/project_build")
    public String buildProject(@PathVariable Long talentId, Model model) {
        model.addAttribute("project", new ProjectCreateDTO());
        model.addAttribute("talent_id", talentId);
        return "project_build";
    }


    @GetMapping(path = "/talents/{talentId}/projects/{projectId}")
    public String showTalentsProject(@PathVariable Long projectId, @PathVariable Long talentId, TalentParamsDTO params, Model model) {

        var projectDTO = projectService.show(projectId);
        var talents = talentService.index(params);
        model.addAttribute("talents", talents);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        return "project";
    }

    @RequestMapping(
            path = "/talents/{talentId}/projects",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createProject(@Valid @ModelAttribute("project") ProjectCreateDTO projectData, @PathVariable Long talentId, Model model, BindingResult bindingResult) {

        projectData.setCreatorId(talentId);
        if (bindingResult.hasErrors()) {
            return "project_build";
        }

        var project = projectRepository.findById(projectService.create(projectData).getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        var talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new ResourceNotFoundException("Talent not found"));
        talent.addProject(project);
        talentRepository.save(talent);
        model.addAttribute("talent_id", talentId);
        return "redirect:/talents/" + talentId;
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}")
    public String updatePhoto(@PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

    @GetMapping(path = "/talents/{talentId}/projects/{id}/update")
    public String updateProjectForm(@PathVariable Long talentId, @PathVariable Long id, Model model) {

        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));

        var projectDTO = projectMapper.map(project);

        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "project_update";
    }

    @RequestMapping(
            path = "/talents/{talentId}/projects/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String updateProject(@Valid @ModelAttribute("user") ProjectCreateDTO projectData, @PathVariable Long talentId, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "project_update";
        }

        var projectUpdate = new ProjectUpdateDTO();

        projectUpdate.setTitle(JsonNullable.of(projectData.getTitle()));
        projectUpdate.setDescription(JsonNullable.of(projectData.getDescription()));

        projectService.update(projectUpdate, id);

        return "redirect:/talents/" + talentId;
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/delete")
    public String deleteProject(@PathVariable Long talentId, @PathVariable Long id) {
        System.out.println("Start Delete");
        projectService.delete(id);
        System.out.println("Back to delete controller");
        return "redirect:/talents/" + talentId;
    }

}