package com.circule.talent.dto.teams;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class TeamDTO {
    private Long id;
    private String title;
    private String description;
    private Set<Long> talentIds;
    private Set<Long> projectIds;
    private Set<Long> packageIds;
    private LocalDate createdAt;
}
