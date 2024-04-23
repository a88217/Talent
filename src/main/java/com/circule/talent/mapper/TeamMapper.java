package com.circule.talent.mapper;

import com.circule.talent.dto.teams.TeamCreateDTO;
import com.circule.talent.dto.teams.TeamDTO;
import com.circule.talent.dto.teams.TeamUpdateDTO;
import com.circule.talent.model.Package;
import com.circule.talent.model.Project;
import com.circule.talent.model.Talent;
import com.circule.talent.model.Team;
import com.circule.talent.repository.PackageRepository;
import com.circule.talent.repository.ProjectRepository;
import com.circule.talent.repository.TalentRepository;
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
public abstract class TeamMapper {
    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PackageRepository packageRepository;


    @Mapping(target = "talents", source = "talentIds", qualifiedByName = "talentIdsToTalents")
    @Mapping(target = "packages", source = "packageIds", qualifiedByName = "packageIdsToPackages")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    public abstract Team map(TeamCreateDTO dto);


    @Mapping(target = "talents", source = "talentIds", qualifiedByName = "talentIdsToTalents")
    @Mapping(target = "packages", source = "packageIds", qualifiedByName = "packageIdsToPackages")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    public abstract void update(TeamUpdateDTO dto, @MappingTarget Team model);

    @Mapping(target = "projectIds", source = "projects", qualifiedByName = "projectsToProjectIds")
    @Mapping(target = "packageIds", source = "packages", qualifiedByName = "packagesToPackageIds")
    @Mapping(target = "talentIds", source = "talents", qualifiedByName = "talentsToTalentIds")
    public abstract TeamDTO map(Team model);

    public abstract List<TeamDTO> map(List<Team> models);


    @Named("projectIdsToProjects")
    public Set<Project> projectIdsToProjects(Set<Long> projectIds) {
        return projectIds == null ? new HashSet<>() : projectRepository.findByIdIn(projectIds);
    }

    @Named("projectsToProjectIds")
    public Set<Long> projectsToProjectIds(Set<Project> projects) {
        return projects.isEmpty() ? new HashSet<>() : projects.stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
    }

    @Named("talentIdsToTalents")
    public Set<Talent> talentIdsToTalents(Set<Long> talentIds) {
        return talentIds == null ? new HashSet<>() : talentRepository.findByIdIn(talentIds);
    }

    @Named("talentsToTalentIds")
    public Set<Long> talentsToTalentIds(Set<Talent> talents) {
        return talents.isEmpty() ? new HashSet<>() : talents.stream()
                .map(Talent::getId)
                .collect(Collectors.toSet());
    }

    @Named("packageIdsToPackages")
    public Set<Package> packageIdsToPackages(Set<Long> packageIds) {
        return packageIds == null ? new HashSet<>() : packageRepository.findByIdIn(packageIds);
    }

    @Named("packagesToPackageIds")
    public Set<Long> packagesToPackageIds(Set<Package> packages) {
        return packages.isEmpty() ? new HashSet<>() : packages.stream()
                .map(Package::getId)
                .collect(Collectors.toSet());
    }

}
