package com.circule.talent.dto.clients;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ClientCreateDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String mobilePhone;

    private String companyName;

    private String about;

    @Size(min = 3)
    private String password;

}
