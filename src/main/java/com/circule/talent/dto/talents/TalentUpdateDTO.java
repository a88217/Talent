package com.circule.talent.dto.talents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TalentUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    private JsonNullable<String> email;

    private JsonNullable<String> about;

    private JsonNullable<Set<Long>> projectIds;

    private JsonNullable<Set<Long>> professionIds;

    @NotBlank
    @Size(min = 3)
    private JsonNullable<String> password;

}
