package com.circule.talent.repository;

import com.circule.talent.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTitle(String name);
    Set<Team> findByIdIn(Set<Long> teamIds);
}
