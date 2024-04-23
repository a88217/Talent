package com.circule.talent.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@DiscriminatorValue("TALENT")
public class Talent extends User {

    private String photoName;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Profession> professions;

    @ManyToMany(mappedBy = "talents", fetch = FetchType.EAGER)
    private Set<Team> teams;

    public void addProject(Project project) {
        projects.add(project);
        project.setCreator(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setCreator(null);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

}
