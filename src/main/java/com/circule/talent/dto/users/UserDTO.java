package com.circule.talent.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobilePhone;
    private LocalDate createdAt;

}
