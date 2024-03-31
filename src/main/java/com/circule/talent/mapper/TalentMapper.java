package com.circule.talent.mapper;

import com.circule.talent.dto.talents.TalentCreateDTO;
import com.circule.talent.dto.talents.TalentDTO;
import com.circule.talent.dto.talents.TalentUpdateDTO;
import com.circule.talent.model.Profession;
import com.circule.talent.model.Project;
import com.circule.talent.model.Talent;
import com.circule.talent.repository.ProfessionRepository;
import com.circule.talent.repository.ProjectRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public abstract class TalentMapper {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProfessionRepository professionRepository;

    @Mapping(target = "professions", source = "professionIds", qualifiedByName = "professionIdsToProfessions")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    @Mapping(target = "passwordDigest", source = "password")
    public abstract Talent map(TalentCreateDTO dto);


    @Mapping(target = "professions", source = "professionIds", qualifiedByName = "professionIdsToProfessions")
    @Mapping(target = "projects", source = "projectIds", qualifiedByName = "projectIdsToProjects")
    @Mapping(target = "passwordDigest", ignore = true)
    public abstract void update(TalentUpdateDTO dto, @MappingTarget Talent model);

    @Mapping(target = "projectIds", source = "projects", qualifiedByName = "projectsToProjectIds")
    @Mapping(target = "professionIds", source = "professions", qualifiedByName = "professionsToProfessionIds")
    public abstract TalentDTO map(Talent model);

    public abstract List<TalentDTO> map(List<Talent> models);

    @Named("professionIdsToProfessions")
    public Set<Profession> professionIdsToProfessions(Set<Long> professionIds) {
        return professionIds == null ? new HashSet<>() : professionRepository.findByIdIn(professionIds);
    }

    @Named("professionsToProfessionIds")
    public Set<Long> professionsToProfessionIds(Set<Profession> professions) {
        return professions.isEmpty() ? new HashSet<>() : professions.stream()
                .map(Profession::getId)
                .collect(Collectors.toSet());
    }

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

    @BeforeMapping
    public void encryptCreatePassword(TalentCreateDTO dto) {
        var password = dto.getPassword();
        dto.setPassword(encoder.encode(password));
    }

    @BeforeMapping
    public void encryptUpdatePassword(TalentUpdateDTO dto, @MappingTarget Talent model) {
        var password = dto.getPassword();
        if (password != null && password.isPresent()) {
            model.setPasswordDigest(encoder.encode(password.get()));
        }
    }
}
