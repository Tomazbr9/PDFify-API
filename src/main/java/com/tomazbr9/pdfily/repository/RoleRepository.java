package com.tomazbr9.pdfily.repository;

import com.tomazbr9.pdfily.enums.RoleName;
import com.tomazbr9.pdfily.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByName(RoleName name);
}
