package com.tomazbr9.pdfily.fileupload.service;

import com.tomazbr9.pdfily.exception.EmptyFileException;
import com.tomazbr9.pdfily.exception.InvalidFileNameException;
import com.tomazbr9.pdfily.exception.ResourceDoesNotBelongToTheAuthenticatedUser;
import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@Service
public class FileValidationService {

    private static final Logger logger = LoggerFactory.getLogger(FileValidationService.class);

    // Verifica se o nome do arquivo é vazio ou não
    public String validateAndGetOriginalName(MultipartFile file){

        String originalName = file.getOriginalFilename();

        if(originalName == null){
            logger.warn("Arquivo sem nome.");
            throw new InvalidFileNameException("Arquivo sem nome.");
        }

        return Paths.get(originalName).getFileName().toString();



    }

    // Verifica se o arquivo enviado é nulo ou vazio
    public void validateFileNotEmpty(MultipartFile file){
        if(file == null || file.isEmpty()) {
            logger.warn("O arquivo enviado está vazio ou é nulo");
            throw new EmptyFileException("O arquivo enviado está vazio ou é nulo");
        }
    }

    public void validateIfUserCanDownload(ConversionModel conversion, UserModel user){
        String username = conversion.getFileUploadModel().getUser().getUsername();
        if(!username.equals(user.getUsername())){
            logger.warn("Usuário {} tentou baixar um arquivo que pertence a {} ", username, user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Usuário sem permissão para fazer download do arquivo");
        }
    }
}
