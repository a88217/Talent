package com.circule.talent.dto.packages;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PackageCreateDTO {
    private String slug;
    private String title;
    private String description;
    private String term;
    private String price;
    private Set<Long> teamIds;
}
