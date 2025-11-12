package com.tomazbr9.pdfily.repository;

import com.tomazbr9.pdfily.model.FileUploadModel;
import com.tomazbr9.pdfily.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadModel, Long> {

}
