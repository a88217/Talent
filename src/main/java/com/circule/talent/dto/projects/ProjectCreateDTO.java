package com.circule.talent.dto.projects;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectCreateDTO {

    @Size(min = 3, max = 1000)
    private String title;

    @Size(min = 3, max = 5000)
    private String description;

    private Long creatorId;

    private Long performerId;

    @Size(max = 5000)
    private String goal;
    @Size(max = 5000)
    private String solution;
    @Size(max = 5000)
    private String result;

}
