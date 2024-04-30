package com.circule.talent.dto.projects;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private Long performerId;
    private String photoName;
    private LocalDate createdAt;
}
