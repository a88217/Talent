package com.circule.talent.dto.teams;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TeamUpdateDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<Set<Long>> talentIds;
    private JsonNullable<Set<Long>> projectIds;
    private JsonNullable<Set<Long>> packageIds;
}
