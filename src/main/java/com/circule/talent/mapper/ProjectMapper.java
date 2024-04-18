package com.circule.talent.mapper;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.model.Project;
import com.circule.talent.model.Talent;
import com.circule.talent.repository.TalentRepository;
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

        @Mapping(target = "creator", source = "creatorId", qualifiedByName = "creatorIdToCreator")
        public abstract Project map(ProjectCreateDTO dto);

        @Mapping(target = "creator", source = "creatorId", qualifiedByName = "creatorIdToCreator")
        public abstract void update(ProjectUpdateDTO dto, @MappingTarget Project model);

        @Mapping(target = "creatorId", source = "creator", qualifiedByName = "creatorToCreatorId")
        public abstract ProjectDTO map(Project model);

        @Named("creatorIdToCreator")
        public Talent creatorIdToCreator(Long creatorId) {
                return creatorId == null ? null : talentRepository.findById(creatorId).get();
        }

        @Named("creatorToCreatorId")
        public Long creatorToCreatorId(Talent creator) {
                return creator.getId();
        }

}
