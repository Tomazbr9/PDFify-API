package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.controller.FileUploadController;
import com.tomazbr9.pdfily.dto.file.FileResponseDTO;
import com.tomazbr9.pdfily.model.FileUploadModel;
import com.tomazbr9.pdfily.model.UserModel;
import com.tomazbr9.pdfily.repository.FileUploadRepository;
import com.tomazbr9.pdfily.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileUploadRepository fileUploadRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${pdfily.upload.temp-dir}")
    private String uploadDir;

    public FileResponseDTO uploadFile(MultipartFile file, UserDetails userDetails){

        UserModel user = findUserByUsername(userDetails.getUsername());

        if(file == null || file.isEmpty()) {
            logger.warn("O arquivo enviado está vazio ou é nulo");
            throw new RuntimeException("O arquivo enviado está vazio ou é nulo");
        }

        Path dirPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Gera um nome seguro e praticamente único para o arquivo que será salvo.
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Resolve o nome do arquivo em relação ao diretório, produzindo o caminho completo onde o arquivo será escrito.
            Path filePath = dirPath.resolve(filename);

            // Copia o conteúdo do MultipartFile para o filePath.
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Arquivo '{}' salvo temporariamente em: {}", file.getOriginalFilename(), filePath);


            FileUploadModel fileUploadModel = new FileUploadModel(
                    UUID.randomUUID(),
                    file.getOriginalFilename(),
                    filePath.toString(),
                    file.getSize(),
                    LocalDateTime.now(),
                    user
            );

            fileUploadRepository.save(fileUploadModel);

            return new FileResponseDTO(
                    fileUploadModel.getId(),
                    fileUploadModel.getOriginalName(),
                    fileUploadModel.getFilePath()
            );

        } catch (IOException error) {
            logger.error("Erro ao salvar o arquivo '{}'", file.getOriginalFilename(), error);
            throw new RuntimeException("Falha ao salvar arquivo temporário", error);
        }

    }

    private UserModel findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

}
