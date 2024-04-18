package com.circule.talent.service;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final TalentRepository talentRepository;

    public List<ProjectDTO> index() {
        var projects = projectRepository.findAll();
        return projects.stream()
                .map(p -> projectMapper.map(p))
                .toList();
    }

    public ProjectDTO show(Long id) {
        var project =  projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
        return projectMapper.map(project);
    }

    public ProjectDTO create(ProjectCreateDTO projectData) {
        var talent = talentRepository.findById(projectData.getCreatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Talent with id " + projectData.getCreatorId() + " not found"));
        var project = projectMapper.map(projectData);
        projectRepository.save(project);
        talent.addProject(project);
        talentRepository.save(talent);
        return projectMapper.map(project);
    }

    public ProjectDTO update(ProjectUpdateDTO projectData, Long id) {
        var project =  projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession with id " + id + " not found"));
        projectMapper.update(projectData, project);
        projectRepository.save(project);
        return projectMapper.map(project);
    }

    public void delete(Long id) {
        System.out.println("Start Delete service");
        System.out.println(projectRepository.findById(id).get().getCreator().getId());

        var talent = projectRepository.findById(id).get().getCreator();
        var project =  projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profession with id " + id + " not found"));
        talent.removeProject(project);
        System.out.println("project deleted from Talent");
        projectRepository.deleteById(id);
        System.out.println("finish Delete service");
    }
}
