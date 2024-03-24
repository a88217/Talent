package com.circule.talent.dto.talents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TalentCreateDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String about;

    private Set<Long> projectIds;

    private Set<Long> professionIds;

    @Size(min = 3)
    private String password;

}
