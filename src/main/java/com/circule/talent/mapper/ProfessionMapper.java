package com.circule.talent.mapper;

import com.circule.talent.dto.professions.ProfessionCreateDTO;
import com.circule.talent.dto.professions.ProfessionDTO;
import com.circule.talent.dto.professions.ProfessionUpdateDTO;
import com.circule.talent.model.Profession;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProfessionMapper {

        public abstract Profession map(ProfessionCreateDTO dto);

        public abstract void update(ProfessionUpdateDTO dto, @MappingTarget Profession model);

        public abstract ProfessionDTO map(Profession model);

}
