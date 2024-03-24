package com.circule.talent.mapper;

import com.circule.talent.dto.projects.ProjectCreateDTO;
import com.circule.talent.dto.projects.ProjectDTO;
import com.circule.talent.dto.projects.ProjectUpdateDTO;
import com.circule.talent.model.Project;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProjectMapper {

        public abstract Project map(ProjectCreateDTO dto);

        public abstract void update(ProjectUpdateDTO dto, @MappingTarget Project model);

        public abstract ProjectDTO map(Project model);

}
