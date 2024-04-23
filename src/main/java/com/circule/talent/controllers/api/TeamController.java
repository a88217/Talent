package com.circule.talent.controllers.api;

import com.circule.talent.dto.teams.TeamCreateDTO;
import com.circule.talent.dto.teams.TeamDTO;
import com.circule.talent.dto.teams.TeamUpdateDTO;
import com.circule.talent.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping(path = "")
    public ResponseEntity<List<TeamDTO>> index() {
        var result = teamService.index();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);
    }

    @GetMapping(path = "/{id}")
    public TeamDTO show(@PathVariable long id) {
        return teamService.show(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDTO create(@Valid @RequestBody TeamCreateDTO teamData) {
        return teamService.create(teamData);
    }

    @PutMapping(path = "/{id}")
    public TeamDTO update(@Valid @RequestBody TeamUpdateDTO teamData, @PathVariable Long id) {
        return teamService.update(teamData, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }

}