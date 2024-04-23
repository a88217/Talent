package com.circule.talent.dto.teams;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeamCreateDTO {
    private String title;
    private String description;
    private Set<Long> talentIds;
    private Set<Long> projectIds;
    private Set<Long> packageIds;
}
