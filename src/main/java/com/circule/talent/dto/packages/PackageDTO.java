package com.circule.talent.dto.packages;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class PackageDTO {
    private Long id;
    private String title;
    private String description;
    private String term;
    private String price;
    private Set<Long> teamIds;
    private LocalDate createdAt;
}