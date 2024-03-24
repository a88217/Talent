package com.circule.talent.service;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

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
        var project = projectMapper.map(projectData);
        projectRepository.save(project);
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
        projectRepository.deleteById(id);
    }
}
