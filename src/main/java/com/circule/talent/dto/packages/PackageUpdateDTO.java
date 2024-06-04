package com.circule.talent.dto.packages;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class PackageUpdateDTO {
    private String slug;
    private JsonNullable<String> title;
    private JsonNullable<String> description;
    private JsonNullable<String> term;
    private JsonNullable<String> price;
    private JsonNullable<Set<Long>> teamIds;
}
