package com.circule.talent.controllers;

import com.circule.talent.dto.packages.PackageCreateDTO;
import com.circule.talent.dto.packages.PackageUpdateDTO;
import com.circule.talent.dto.talents.TalentParamsDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.PackageMapper;
import com.circule.talent.model.User;
import com.circule.talent.repository.PackageRepository;
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
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackagesController {

    private final PackageService packageService;

    private final TeamService teamService;

    private final TalentService talentService;

    private final ProjectService projectService;

    private final PackageRepository packageRepository;

    private final PackageMapper packageMapper;

    private final UserUtils userUtils;

    @GetMapping(path = "")
    public String index(Model model) {
        var packages = packageService.index();
        var teams = teamService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("packages", packages);
        model.addAttribute("teams", teams);
        return "services";
    }

    @GetMapping(path = "/{id}")
    public String show(TalentParamsDTO params, @PathVariable Long id, Model model) {

        var packageDTO = packageService.show(id);
        var teams = teamService.index();
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("package", packageDTO);
        model.addAttribute("teams", teams);
        return "package";
    }

    @GetMapping(path = "/content_strategy")
    public String showContentStrategy(TalentParamsDTO params, Model model) {

        var packageDTO = packageService.show("content_strategy");
        var teams = teamService.index();
        var talents = talentService.index(params);
        var projects = projectService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("package", packageDTO);
        model.addAttribute("teams", teams);
        model.addAttribute("talents", talents);
        model.addAttribute("projects", projects);
        return "content_strategy";
    }

    @GetMapping(path = "/brand_platform")
    public String showBrandPlatform(TalentParamsDTO params, Model model) {

        var packageDTO = packageService.show("brand_platform");
        var teams = teamService.index();
        var talents = talentService.index(params);
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("package", packageDTO);
        model.addAttribute("teams", teams);
        model.addAttribute("talents", talents);
        return "brand_platform";
    }

    @GetMapping(path = "/build")
    public String buildTeam(Model model) {

        var teams = teamService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);

        model.addAttribute("teams", teams);
        model.addAttribute("package", new PackageCreateDTO());
        return "package_build";
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String createPackage(@Valid @ModelAttribute("package") PackageCreateDTO packageData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "package_build";
        }

        packageService.create(packageData);
        return "redirect:/packages";
    }

    @GetMapping(path = "/update/{id}")
    public String updatePackageForm(@PathVariable Long id, Model model) {

        var pack = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package with id " + id + " not found"));
        var packageDTO = packageMapper.map(pack);
        var teams = teamService.index();
        var user = Objects.nonNull(userUtils.getCurrentUser()) ? userUtils.getCurrentUser() : new User();
        model.addAttribute("user", user);
        model.addAttribute("package", packageDTO);
        model.addAttribute("teams", teams);
        return "package_update";
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            })
    public String updatePackage(@Valid @ModelAttribute("package") PackageCreateDTO packageData, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "package_update";
        }

        var packageUpdate = new PackageUpdateDTO();

        packageUpdate.setTitle(JsonNullable.of(packageData.getTitle()));
        packageUpdate.setDescription(JsonNullable.of(packageData.getDescription()));
        packageUpdate.setTerm(JsonNullable.of(packageData.getTerm()));
        packageUpdate.setPrice(JsonNullable.of(packageData.getPrice()));
        packageUpdate.setTeamIds(JsonNullable.of(packageData.getTeamIds()));

        packageService.update(packageUpdate, id);

        return "redirect:/packages/" + id;
    }

}
