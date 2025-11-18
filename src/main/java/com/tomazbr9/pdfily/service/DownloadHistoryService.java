package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.downloadDTO.DownloadRequestDTO;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;

import com.tomazbr9.pdfily.exception.ConversionNotFoundException;
import com.tomazbr9.pdfily.exception.ResourceDoesNotBelongToTheAuthenticatedUser;
import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.model.ConversionModel;
import com.tomazbr9.pdfily.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.model.UserModel;
import com.tomazbr9.pdfily.repository.ConversionRepository;
import com.tomazbr9.pdfily.repository.DownloadHistoryRepository;
import com.tomazbr9.pdfily.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DownloadHistoryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    DownloadHistoryRepository downloadHistoryRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistoryService.class);

    public DownloadResponseDTO saveDownloadToHistory(DownloadRequestDTO request, UserDetails userDetails){

        ConversionModel conversion = getConversion(request.conversionId());
        UserModel user = getUser(userDetails.getUsername());

        validateIfUserCanDownload(conversion, user);

        saveDownload(conversion, user);

        logger.info("Download salvo no historico com sucesso!");

        return new DownloadResponseDTO(conversion.getId());

    }

    private UserModel getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrato."));
    }

    private ConversionModel getConversion(UUID uuid){
        return conversionRepository.findById(uuid).orElseThrow(() -> new ConversionNotFoundException("Conversão não encontrada."));
    }

    private void validateIfUserCanDownload(ConversionModel conversion, UserModel user){
        String username = conversion.getFileUploadModel().getUser().getUsername();
        if(!username.equals(user.getUsername())){
            logger.warn("Usuário {} tentou baixar um arquivo que pertence a {} ", username, user.getUsername());
            throw new ResourceDoesNotBelongToTheAuthenticatedUser("Usuário sem permissão para fazer download do arquivo");
        }
    }

    private void saveDownload(ConversionModel conversion, UserModel user){

        DownloadHistoryModel downloadHistoryModel = DownloadHistoryModel.builder()
                .conversionId(conversion.getId())
                .userID(user)
                .downloadedAt(LocalDateTime.now())
                .build();

        downloadHistoryRepository.save(downloadHistoryModel);

    }

}
