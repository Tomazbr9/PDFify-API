package com.tomazbr9.pdfily.conversion.service;

import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.exception.*;
import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.fileupload.repository.FileUploadRepository;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.util.FileNamingUtil;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ConversionService {

    @Autowired FileUploadRepository fileUploadRepository;

    @Autowired UserRepository userRepository;

    @Autowired ConversionValidationService conversionValidationService;

    @Autowired ConversionEngineService conversionEngineService;

    @Autowired ConversionMetadataFactory conversionMetadataFactory;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionService.class);

    @Transactional
    public ConvertResponseDTO convertToPDF(ConvertRequestDTO request, UserDetails userDetails){

        FileUploadModel fileUploadModel = getFileUpload(request.fileId());

        UserModel user = getUser(userDetails.getUsername());

        conversionValidationService.validatedIfFileBelongsAuthenticatedUser(fileUploadModel, user);

        Path input = Paths.get(fileUploadModel.getFilePath());

        conversionValidationService.validateFileExists(input);

        String outputFilename = FileNamingUtil.generateSafeFilename("pdf");

        Path output = input.getParent().resolve(outputFilename);

        try {

            conversionEngineService.convert(input, output);

            Double SizeFileInMG = FileNamingUtil.calculateFileSizeInMB(Files.size(output));

            ConversionModel saved = conversionMetadataFactory.createSuccess(fileUploadModel, outputFilename, output, SizeFileInMG);

            logger.info("Arquivo {} convertido com sucesso.", output);

            return new ConvertResponseDTO(saved.getId(), outputFilename, saved.getStatus().name());


        } catch (Exception error) {
            logger.error("Erro ao converter o arquivo: {}", fileUploadModel.getOriginalName(), error);
            conversionMetadataFactory.createFailure(fileUploadModel, output);
            throw new ConvertingFileException("Erro ao converter arquivo.");
        }
    }

    private FileUploadModel getFileUpload(UUID fileId){
        return fileUploadRepository.findById(fileId).orElseThrow(() -> new FileUploadNotFoundException("Arquivo não encontrado."));
    }

    private UserModel getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    }


}
