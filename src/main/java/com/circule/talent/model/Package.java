package com.circule.talent.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "packages")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Package implements BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private long id;

    @ToString.Include
    private String title;

    @ToString.Include
    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 2000)
    private String term;

    private String price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "packages_teams",
            joinColumns = @JoinColumn(
                    name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "team_id", referencedColumnName = "id"))
    private Set<Team> teams;

    @CreatedDate
    private LocalDate createdAt;

    public void addTeam(Team team) {
        teams.add(team);
        team.addPackage(this);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
        team.removePackage(this);
    }
}
