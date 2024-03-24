package com.circule.talent.dto.professions;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfessionCreateDTO {

    @Size(min = 3, max = 1000)
    private String title;

    @Size(min = 3, max = 5000)
    private String description;

}
