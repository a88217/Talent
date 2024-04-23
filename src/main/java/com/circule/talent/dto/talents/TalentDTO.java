package com.circule.talent.dto.talents;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class TalentDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String about;

    private String mobilePhone;

    private String photoName;

    private Set<Long> projectIds;

    private Set<Long> professionIds;

    private Set<Long> teamIds;

    private LocalDate createdAt;

}
