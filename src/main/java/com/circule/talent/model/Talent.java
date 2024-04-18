package com.circule.talent.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DiscriminatorValue("TALENT")
public class Talent extends User {

    private String photoName;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Profession> professions;

    public void addProject(Project project) {
        projects.add(project);
        project.setCreator(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setCreator(null);
    }

}
