package com.circule.talent.dto.projects;

import jakarta.persistence.Column;
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
    private String goal;
    private String solution;
    private String result;
    private String coverName;
    private String photo1Name;
    private String photo2Name;
    private String photo3Name;
    private String photo4Name;
    private String photo5Name;
    private LocalDate createdAt;
}
