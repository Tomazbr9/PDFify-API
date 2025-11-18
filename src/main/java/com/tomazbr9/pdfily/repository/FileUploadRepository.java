package com.tomazbr9.pdfily.repository;

import com.tomazbr9.pdfily.model.FileUploadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadModel, UUID> {

}
