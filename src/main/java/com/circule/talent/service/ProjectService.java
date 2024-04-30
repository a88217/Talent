package com.circule.talent.service;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.ProjectMapper;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final TalentRepository talentRepository;

    private final TeamRepository teamRepository;

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

    public ProjectDTO createTeamProject(ProjectCreateDTO projectData) {
        var team = teamRepository.findById(projectData.getPerformerId())
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + projectData.getPerformerId() + " not found"));
        var project = projectMapper.map(projectData);
        projectRepository.save(project);
        team.addProject(project);
        teamRepository.save(team);
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

        var project =  projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));

        var talent = projectRepository.findById(id).get().getCreator();
        var team = projectRepository.findById(id).get().getPerformer();

        if(Objects.nonNull(talent)) {
            talent.removeProject(project);
        }
        if(Objects.nonNull(team)) {
            team.removeProject(project);
        }

        projectRepository.deleteById(id);
    }
}
