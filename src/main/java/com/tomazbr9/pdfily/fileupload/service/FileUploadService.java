package com.tomazbr9.pdfily.fileupload.service;

import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.exception.*;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.fileupload.repository.FileUploadRepository;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tomazbr9.pdfily.util.FileNamingUtil;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class FileUploadService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Autowired
    FileValidationService fileValidationService;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    FilePathGeneratorService filePathGeneratorService;

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${pdfily.upload.temp-dir}")
    private String uploadDir;

    public FileResponseDTO uploadFile(MultipartFile file, UserDetails userDetails){

        fileValidationService.validateFileNotEmpty(file);

        String originalFileName = fileValidationService.validateAndGetOriginalName(file);

        UserModel user = getUser(userDetails.getUsername());

        String extension = FileNamingUtil.getSafeExtension(originalFileName);

        String newFileName = FileNamingUtil.generateSafeFilename(extension);

        Path dirPath = filePathGeneratorService.transformInPath(uploadDir);

        Path filePath = filePathGeneratorService.generatePath(dirPath, newFileName);

        fileStorageService.createDirectoryIfNeeded(dirPath);

        fileStorageService.saveFileInDisk(file, filePath);

        Double fileSizeInMG = FileNamingUtil.calculateFileSizeInMB(file.getSize());

        FileUploadModel saved = savedFileMetaData(originalFileName, filePath, fileSizeInMG, user);

        logger.info("Arquivo '{}' salvo temporariamente em: {}", originalFileName, filePath);

        return new FileResponseDTO(
                saved.getId(),
                saved.getOriginalName()
        );

    }

    // Retorna o usuário autenticado
    private UserModel getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }

    // Salva as informações do arquivo enviao no banco de dados
    private FileUploadModel savedFileMetaData (String originalName, Path filePath, Double size, UserModel user){

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
