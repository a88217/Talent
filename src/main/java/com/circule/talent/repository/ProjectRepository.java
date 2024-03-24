package com.circule.talent.repository;

import com.circule.talent.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitle(String name);
    Set<Project> findByIdIn(Set<Long> projectIds);
}
