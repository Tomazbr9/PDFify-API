package com.tomazbr9.pdfily.fileupload.service;

import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.fileupload.repository.FileUploadRepository;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class FileUploadMetadataFactoryService {

    @Autowired
    FileUploadRepository fileUploadRepository;

    public FileUploadModel savedFileMetaData (String originalName, Path filePath, Double size, UserModel user){

        FileUploadModel fileUploadModel = FileUploadModel.builder()
                .originalName(originalName)
                .filePath(filePath.toString())
                .fileSize(size)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return fileUploadRepository.save(fileUploadModel);

    }
}
