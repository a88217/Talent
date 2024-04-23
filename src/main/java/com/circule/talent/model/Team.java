package com.circule.talent.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "teams")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team implements BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @ToString.Include
    private String title;

    @ToString.Include
    @Column(length = 2000)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teams_talents",
            joinColumns = @JoinColumn(
                    name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "talent_id", referencedColumnName = "id"))
    private Set<Talent> talents = new HashSet<>();

    @OneToMany(mappedBy = "performer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Project> projects;

    @ManyToMany(mappedBy = "teams", fetch = FetchType.EAGER)
    private Set<Package> packages;

    @CreatedDate
    private LocalDate createdAt;

    public void addProject(Project project) {
        projects.add(project);
        project.setPerformer(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setPerformer(null);
    }

    public void addTalent(Talent talent) {
        talents.add(talent);
        talent.addTeam(this);
    }

    public void removeTalent(Talent talent) {
        talents.remove(talent);
        talent.removeTeam(this);
    }

    public void addPackage(Package pack) {
        packages.add(pack);
    }

    public void removePackage(Package pack) {
        packages.remove(pack);
    }

}
