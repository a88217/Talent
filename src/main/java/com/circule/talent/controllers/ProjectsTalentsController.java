package com.circule.talent.controllers;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.model.User;
import com.circule.talent.repository.ProjectRepository;
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
@RequiredArgsConstructor
public class ProjectsTalentsController {

    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final TalentService talentService;

    private final TalentRepository talentRepository;

    private final ProjectMapper projectMapper;

    private final ProfessionService professionService;

    private final UserUtils userUtils;


    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping(path = "/projects")
    public String index(TalentParamsDTO params, Model model) {
        var talents = talentService.index(params);
        var projects = projectService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("professions", projects);
        return "projects";
    }

    @GetMapping(path = "/projects/{projectId}")
    public String show(TalentParamsDTO params, @PathVariable Long projectId, Model model) {

        var projectDTO = projectService.show(projectId);
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("project", projectDTO);
        return "project";
    }

    @GetMapping(path = "/talents/{talentId}/project_build")
    public String buildTalentProject(@PathVariable Long talentId, Model model) {
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", new ProjectCreateDTO());
        model.addAttribute("talent_id", talentId);
        return "project_create_bootstrap";
    }


    @GetMapping(path = "/talents/{talentId}/projects/{projectId}")
    public String showTalentsProject(@PathVariable Long projectId, @PathVariable Long talentId, TalentParamsDTO params, Model model) {

        var projectDTO = projectService.show(projectId);
        var talents = talentService.index(params);
        var talent = talentService.show(talentId);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        var projects = projectService.index();
        model.addAttribute("user", user);
        model.addAttribute("talents", talents);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("projects", projects);
        model.addAttribute("talent", talent);
        return "project_bootstrap";
    }

    @RequestMapping(
            path = "/talents/{talentId}/projects",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createTalentProject(@Valid @ModelAttribute("project") ProjectCreateDTO projectData, @PathVariable Long talentId, Model model, BindingResult bindingResult) {

        projectData.setCreatorId(talentId);
        if (bindingResult.hasErrors()) {
            return "project_create_bootstrap";
        }

        var project = projectRepository.findById(projectService.create(projectData).getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        var talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new ResourceNotFoundException("Talent not found"));
        talent.addProject(project);
        talentRepository.save(talent);
        model.addAttribute("talent_id", talentId);
        return "redirect:/talents/" + talentId + "/profile/portfolio";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/update")
    public String updateProjectPhoto(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setCoverName(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/photo1")
    public String updateProjectPhoto1(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setPhoto1Name(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/photo2")
    public String updateProjectPhoto2(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setPhoto2Name(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/photo3")
    public String updateProjectPhoto3(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setPhoto3Name(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/photo4")
    public String updateProjectPhoto4(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setPhoto4Name(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/photo5")
    public String updateProjectPhoto5(@PathVariable Long talentId, @PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
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

            project.setPhoto5Name(resultFilename);
            projectRepository.save(project);
        }
        var projectDTO = projectService.show(id);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @GetMapping(path = "/talents/{talentId}/projects/{id}/update")
    public String updateTalentProjectForm(@PathVariable Long talentId, @PathVariable Long id, Model model) {

        var project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));

        var projectDTO = projectMapper.map(project);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("project", projectDTO);
        model.addAttribute("talent_id", talentId);
        model.addAttribute("project_id", id);
        return "talent_project_update";
    }

    @RequestMapping(
            path = "/talents/{talentId}/projects/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String updateTalentProject(@Valid @ModelAttribute("user") ProjectCreateDTO projectData, @PathVariable Long talentId, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "project_update";
        }

        var projectUpdate = new ProjectUpdateDTO();

        projectUpdate.setTitle(JsonNullable.of(projectData.getTitle()));
        projectUpdate.setDescription(JsonNullable.of(projectData.getDescription()));
        projectUpdate.setGoal(JsonNullable.of(projectData.getGoal()));
        projectUpdate.setSolution(JsonNullable.of(projectData.getSolution()));
        projectUpdate.setResult(JsonNullable.of(projectData.getResult()));

        projectService.update(projectUpdate, id);

        return "redirect:/talents/" + talentId + "/portfolio";
    }

    @PostMapping(path = "/talents/{talentId}/projects/{id}/delete")
    public String deleteTalentProject(@PathVariable Long talentId, @PathVariable Long id) {
        System.out.println("Start Delete");
        projectService.delete(id);
        System.out.println("Back to delete controller");
        return "redirect:/talents/" + talentId + "/profile/portfolio";
    }

}