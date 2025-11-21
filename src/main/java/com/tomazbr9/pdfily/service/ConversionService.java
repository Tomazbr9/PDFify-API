package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.enums.StatusName;
import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.exception.*;
import com.tomazbr9.pdfily.model.ConversionModel;
import com.tomazbr9.pdfily.model.FileUploadModel;
import com.tomazbr9.pdfily.model.UserModel;
import com.tomazbr9.pdfily.repository.ConversionRepository;
import com.tomazbr9.pdfily.repository.FileUploadRepository;
import com.tomazbr9.pdfily.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jodconverter.core.DocumentConverter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@DependsOn("officeManager")
public class ConversionService {

    @Autowired
    DocumentConverter converter;

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    UserRepository userRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionService.class);

    @Transactional
    public ConvertResponseDTO convertToPDF(ConvertRequestDTO request, UserDetails userDetails){

        FileUploadModel fileUploadModel = getFileUpload(request.fileId());

        UserModel user = getUser(userDetails.getUsername());

        validatedIfFileBelongsAuthenticatedUser(fileUploadModel, user);

        Path input = Paths.get(fileUploadModel.getFilePath());

        validateFileExists(input);

        String outputFilename = generateSafeFilenamePDF();

        Path output = input.getParent().resolve(outputFilename);

        try {
            converter.convert(
                    input.toFile())
                    .to(output.toFile())
                    .execute();

            ConversionModel saved = savedConvertedFileMetaData(fileUploadModel, output, StatusName.SUCCESS);

            logger.info("Arquivo {} convertido com sucesso.", output);

            return new ConvertResponseDTO(saved.getId(), saved.getStatus().name());

        } catch (Exception error) {
            logger.error("Erro ao converter o arquivo: {}", fileUploadModel.getOriginalName(), error);
            ConversionModel saved = savedConvertedFileMetaData(fileUploadModel, output, StatusName.FAILURE);
            throw new ConvertingFileException("Erro ao converter arquivo.");
        }
    }

    private FileUploadModel getFileUpload(UUID fileId){
        return fileUploadRepository.findById(fileId).orElseThrow(() -> new FileUploadNotFoundException("Arquivo não encontrado."));
    }

    private UserModel getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }

    private void validatedIfFileBelongsAuthenticatedUser(FileUploadModel fileUploadModel, UserModel user) {
        String username = fileUploadModel.getUser().getUsername();
        if (!username.equals(user.getUsername())){
            logger.info("Usuário {} sem permissão para converter arquivo.", user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Você não tem permissão para converter esse arquivo");
        }
    }

    private void validateFileExists(Path input){
        if (!Files.exists(input)) {
            logger.error("Arquivo temporário expirado ou inexistente");
            throw new ExpiredOrNonExistentFile("Arquivo temporário expirado ou inexistente.");
        }
    }

    private String generateSafeFilenamePDF(){
        return UUID.randomUUID().toString() + ".pdf";
    }

    private ConversionModel savedConvertedFileMetaData(FileUploadModel fileUploadModel, Path output, StatusName status){

        ConversionModel conversionModel = ConversionModel.builder()
                .fileUploadModel(fileUploadModel)
                .targetFormat(TargetFormat.PDF)
                .status(status)
                .outputPath(output.toString())
                .createdAt(LocalDateTime.now())
                .build();

        return conversionRepository.save(conversionModel);

    }


}
