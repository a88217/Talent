package com.circule.talent.repository;

import com.circule.talent.model.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findByEmail(String email);
}
