package com.circule.talent.dto.talents;


import com.circule.talent.model.Profession;
import com.circule.talent.model.Project;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

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

    private Set<Long> projectIds;

    private Set<Long> professionIds;

    private LocalDate createdAt;

}
