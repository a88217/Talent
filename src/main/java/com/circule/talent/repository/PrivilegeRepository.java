package com.circule.talent.repository;

import com.circule.talent.model.Privilege;
import com.circule.talent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
