package com.circule.talent.dto.clients;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class ClientUpdateDTO {

    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

    private JsonNullable<String> email;

    private JsonNullable<String> about;

    private JsonNullable<String> mobilePhone;

    private JsonNullable<String> companyName;

    @NotBlank
    @Size(min = 3)
    private JsonNullable<String> password;

}
