package com.tomazbr9.pdfily.conversion.service;

import com.tomazbr9.pdfily.exception.ExpiredOrNonExistentFile;
import com.tomazbr9.pdfily.exception.ResourceDoesNotBelongToTheAuthenticatedUser;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ConversionValidationService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionValidationService.class);

    public  void validatedIfFileBelongsAuthenticatedUser(FileUploadModel fileUploadModel, UserModel user) {
        String username = fileUploadModel.getUser().getUsername();
        if (!username.equals(user.getUsername())){
            logger.info("Usuário {} sem permissão para converter arquivo.", user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Você não tem permissão para converter esse arquivo");
        }
    }

    public void validateFileExists(Path input){
        if (!Files.exists(input)) {
            logger.error("Arquivo temporário expirado ou inexistente");
            throw new ExpiredOrNonExistentFile("Arquivo temporário expirado ou inexistente.");
        }
    }
}
