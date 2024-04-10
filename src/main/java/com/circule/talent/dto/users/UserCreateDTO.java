package com.circule.talent.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {

    @Email
    private String email;

    private String firstName;

    private String lastName;

    private String mobilePhone;

    @NotBlank
    @Size(min = 3)
    private String password;

}
