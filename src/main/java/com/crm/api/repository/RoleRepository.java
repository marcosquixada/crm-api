package com.crm.api.repository;


import com.crm.api.model.ERole;
import com.crm.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByName(ERole name);

  boolean existsByName(ERole eRole);
}