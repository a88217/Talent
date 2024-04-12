package com.circule.talent.controllers;

import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.service.ProfessionService;
import com.circule.talent.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/talents")
@RequiredArgsConstructor
public class TalentsController {

    private final TalentService talentService;

    private final TalentRepository talentRepository;



    private final ProfessionService professionService;

    @Value("${upload.path}")
    private String uploadPath;

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

        var talentDTO = talentService.show(id);
        var professions = professionService.index();
        model.addAttribute("talent", talentDTO);
        model.addAttribute("professions", professions);
        return "talent";
    }

    @PostMapping(path = "/{id}")
    public String update(@PathVariable Long id, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        var talent = talentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + id + " not found"));
        var professions = professionService.index();
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            System.out.println(uploadPath);
            System.out.println(uploadPath + "/" + resultFilename);

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            talent.setPhotoName(resultFilename);
            talentRepository.save(talent);
        }
        var talentDTO = talentService.show(id);
        model.addAttribute("talent", talentDTO);
        model.addAttribute("professions", professions);
        return "talent";
    }

}