package com.circule.talent.mapper;

import com.circule.talent.dto.packages.PackageCreateDTO;
import com.circule.talent.dto.packages.PackageDTO;
import com.circule.talent.dto.packages.PackageUpdateDTO;
import com.circule.talent.model.Package;
import com.circule.talent.model.Team;
import com.circule.talent.repository.TeamRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PackageMapper {
    @Autowired
    private TeamRepository teamRepository;

    @Mapping(target = "teams", source = "teamIds", qualifiedByName = "teamIdsToTeams")
    public abstract Package map(PackageCreateDTO dto);


    @Mapping(target = "teams", source = "teamIds", qualifiedByName = "teamIdsToTeams")
    public abstract void update(PackageUpdateDTO dto, @MappingTarget Package model);

    @Mapping(target = "teamIds", source = "teams", qualifiedByName = "teamsToTeamIds")
    public abstract PackageDTO map(Package model);

    public abstract List<PackageDTO> map(List<Package> models);


    @Named("teamIdsToTeams")
    public Set<Team> teamIdsToTeams(Set<Long> teamIds) {
        return teamIds == null ? new HashSet<>() : teamRepository.findByIdIn(teamIds);
    }

    @Named("teamsToTeamIds")
    public Set<Long> teamsToTeamIds(Set<Team> teams) {
        return teams.isEmpty() ? new HashSet<>() : teams.stream()
                .map(Team::getId)
                .collect(Collectors.toSet());
    }
}
