package com.circule.talent.service;

import com.circule.talent.dto.teams.TeamCreateDTO;
import com.circule.talent.dto.teams.TeamDTO;
import com.circule.talent.dto.teams.TeamUpdateDTO;
import com.circule.talent.exception.ResourceNotFoundException;
import com.circule.talent.mapper.TeamMapper;
import com.circule.talent.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    public List<TeamDTO> index() {
        return teamMapper.map(teamRepository.findAll());
    }

    public TeamDTO show(Long id) {
        var team =  teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + id + " not found"));
        return teamMapper.map(team);
    }

    public TeamDTO create(TeamCreateDTO teamData) {
        var team = teamMapper.map(teamData);
        teamRepository.save(team);
        return teamMapper.map(team);
    }

    public TeamDTO update(TeamUpdateDTO teamData, Long id) {
        var team =  teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team with id " + id + " not found"));
        teamMapper.update(teamData, team);
        teamRepository.save(team);
        return teamMapper.map(team);
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }
}
