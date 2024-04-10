package com.circule.talent.dto.clients;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobilePhone;

    private String companyName;

    private String about;

    private LocalDate createdAt;

}
