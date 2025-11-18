package com.tomazbr9.pdfily.repository;

import com.tomazbr9.pdfily.enums.RoleName;
import com.tomazbr9.pdfily.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DownloadHistoryRepository extends JpaRepository<DownloadHistoryModel, UUID> {

}
