package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.exception.*;
import com.tomazbr9.pdfily.model.FileUploadModel;
import com.tomazbr9.pdfily.model.UserModel;
import com.tomazbr9.pdfily.repository.FileUploadRepository;
import com.tomazbr9.pdfily.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
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

        validateFileNotEmpty(file);

        String originalFileName = validateAndGetOriginalName(file);

        UserModel user = getUser(userDetails.getUsername());

        String extension = getSafeExtension(originalFileName);

        String newFileName = generateSafeFilename(extension);

        Path dirPath = Path.of(uploadDir);
        Path filePath = dirPath.resolve(newFileName);

        createDirectoryIfNeeded(dirPath);

        saveFileInDisk(file, filePath);

        Double fileSizeInMG = getFileSizeInMB(file.getSize());

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

    // Verifica se o arquivo enviado é nulo ou vazio
    private void validateFileNotEmpty(MultipartFile file){
        if(file == null || file.isEmpty()) {
            logger.warn("O arquivo enviado está vazio ou é nulo");
            throw new EmptyFileException("O arquivo enviado está vazio ou é nulo");
        }
    }

    // Verifica se o nome do arquivo é vazio ou não
    private String validateAndGetOriginalName(MultipartFile file){

        String originalName = file.getOriginalFilename();

        if(originalName == null){
            logger.warn("Arquivo sem nome.");
            throw new InvalidFileNameException("Arquivo sem nome.");
        }

        return originalName;

    }

    // Obtêm a extensão do nome do arquivo
    private String getSafeExtension(String originalName){

        String extension = FilenameUtils.getExtension(originalName);

        if(extension == null || extension.isBlank()){
            logger.warn("O arquivo {} não possui extensão.", originalName);
            throw new UnsupportedFileFormatException("O arquivo não possui extensão.");
        }

        if(!TargetFormat.isSupported(extension)){
            logger.warn("Formato do arquivo enviado não é suportado: " + extension);
            throw new UnsupportedFileFormatException("Formato não suportado: " + extension);
        }

        return extension.toLowerCase();
    }

    // Gera um novo nome para o arquivo enviado
    private String generateSafeFilename(String extension){
        return UUID.randomUUID().toString() + "." + extension;
    }

    // Cria um diretorio temporario caso ele não exista
    private void createDirectoryIfNeeded(Path dirPath){
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        }
        catch(IOException error){
            logger.error("Erro ao criar diretorio: '{}'", dirPath, error);
            throw new FailedToSaveTemporaryFileException("Erro ao criar diretorio temporario.");
            }
    }

    // Copia o conteudo do arquivo enviado para o diretorio
    private void saveFileInDisk(MultipartFile file, Path filePath) {
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException error){
            logger.error("Erro ao salvar arquivo {} em disco.", filePath, error);
            throw new FailedToSaveTemporaryFileException("Falha ao salvar arquivo temporário.");
        }
    }

    private Double getFileSizeInMB(long fileSizeInByts){
        return  (double) fileSizeInByts / (1024 * 1024);
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
