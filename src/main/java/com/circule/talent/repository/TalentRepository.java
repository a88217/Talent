package com.circule.talent.repository;

import com.circule.talent.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findByEmail(String email);
    Set<Talent> findByIdIn(Set<Long> talentIds);
}
