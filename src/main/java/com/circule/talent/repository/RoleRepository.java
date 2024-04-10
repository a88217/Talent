package com.circule.talent.repository;

import com.circule.talent.model.Project;
import com.circule.talent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
