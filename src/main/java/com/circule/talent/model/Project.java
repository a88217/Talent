package com.circule.talent.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project implements BaseEntity {

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

    @Column(length = 5000)
    private String goal;

    @Column(length = 5000)
    private String solution;

    @Column(length = 5000)
    private String result;

    private String coverName;
    private String photo1Name;
    private String photo2Name;
    private String photo3Name;
    private String photo4Name;
    private String photo5Name;

    @ManyToOne
    private Talent creator;

    @ManyToOne
    private Team performer;

    @CreatedDate
    private LocalDate createdAt;

}
