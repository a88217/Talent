package com.circule.talent.dto.professions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ProfessionDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate createdAt;
}
