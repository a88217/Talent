package com.circule.talent.repository;

import com.circule.talent.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findByTitle(String name);
    Set<Package> findByIdIn(Set<Long> packageIds);
}
