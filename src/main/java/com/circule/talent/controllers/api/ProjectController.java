package com.circule.talent.controllers.api;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService  projectService;

    @GetMapping(path = "")
    public ResponseEntity<List<ProjectDTO>> index() {
        var result = projectService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public ProjectDTO show(@PathVariable long id) {
        return projectService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO create(@Valid @RequestBody ProjectCreateDTO projectData) {
        return projectService.create(projectData);
    }

    @PutMapping(path = "/{id}")
    public ProjectDTO update(@Valid @RequestBody ProjectUpdateDTO projectData, @PathVariable Long id) {
        return projectService.update(projectData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projectService.delete(id);
    }

}
