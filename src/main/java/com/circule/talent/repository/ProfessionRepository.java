package com.circule.talent.repository;

import com.circule.talent.model.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    Optional<Profession> findByTitle(String name);
    Set<Profession> findByIdIn(Set<Long> professionIds);
}
