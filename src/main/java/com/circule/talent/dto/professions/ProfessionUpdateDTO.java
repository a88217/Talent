package com.circule.talent.dto.professions;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
public class ProfessionUpdateDTO {

    @Size(min = 3, max = 1000)
    private JsonNullable<String> title;

    @Size(min = 3, max = 5000)
    private JsonNullable<String> description;
}
