package com.circule.talent.mapper;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.model.Project;
import com.circule.talent.model.Talent;
import com.circule.talent.model.Team;
import com.circule.talent.repository.TalentRepository;
import com.circule.talent.repository.TeamRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProjectMapper {

        @Autowired
        private TalentRepository talentRepository;

        @Autowired
        TeamRepository teamRepository;

        @Mapping(target = "creator", source = "creatorId", qualifiedByName = "creatorIdToCreator")
        @Mapping(target = "performer", source = "performerId", qualifiedByName = "performerIdToPerformer")
        public abstract Project map(ProjectCreateDTO dto);

        @Mapping(target = "creator", source = "creatorId", qualifiedByName = "creatorIdToCreator")
        @Mapping(target = "performer", source = "performerId", qualifiedByName = "performerIdToPerformer")
        public abstract void update(ProjectUpdateDTO dto, @MappingTarget Project model);

        @Mapping(target = "creatorId", source = "creator", qualifiedByName = "creatorToCreatorId")
        @Mapping(target = "performerId", source = "performer", qualifiedByName = "performerToPerformerId")
        public abstract ProjectDTO map(Project model);

        @Named("creatorIdToCreator")
        public Talent creatorIdToCreator(Long creatorId) {
                return creatorId == null ? null : talentRepository.findById(creatorId).get();
        }

        @Named("creatorToCreatorId")
        public Long creatorToCreatorId(Talent creator) {
                return creator == null ? null : creator.getId();
        }

        @Named("performerIdToPerformer")
        public Team performerIdToPerformer(Long performerId) {
                return performerId == null ? null : teamRepository.findById(performerId).get();
        }

        @Named("performerToPerformerId")
        public Long performerToPerformerId(Team performer) {
                return performer == null ? null : performer.getId();
        }

}
