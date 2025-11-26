package com.tomazbr9.pdfily.downloadhistory.service;

import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.exception.ConversionNotFoundException;
import com.tomazbr9.pdfily.exception.DownloadLogNotFoundException;
import com.tomazbr9.pdfily.exception.ResourceDoesNotBelongToTheAuthenticatedUser;
import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.conversion.repository.ConversionRepository;
import com.tomazbr9.pdfily.downloadhistory.repository.DownloadHistoryRepository;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DownloadHistoryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    DownloadHistoryRepository downloadHistoryRepository;

    @Autowired
    DownloadValidationService downloadValidationService;

    @Autowired
    DownloadHistorySavingService downloadHistorySavingService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistoryService.class);

    public void saveDownloadToHistory(UUID conversionId, UserDetails userDetails){

        ConversionModel conversion = getConversion(conversionId);
        UserModel user = getUser(userDetails.getUsername());

        downloadValidationService.validateIfUserCanDownload(conversion, user);

        downloadHistorySavingService.saveDownload(conversion, user);

    }

    public List<DownloadResponseDTO> getAllDownloadHistory(UserDetails userDetails){
        UserModel user = getUser(userDetails.getUsername());

        List<DownloadHistoryModel> downloadList = downloadHistoryRepository.findDownloadHistoryByUser(user);

        return downloadList.stream()
                .map(downloadLog -> new DownloadResponseDTO(
                        downloadLog.getId(),
                        downloadLog.getConversion().getFileUploadModel().getOriginalName(),
                        String.valueOf(downloadLog.getConversion().getFileUploadModel().getFileSize() + " MB"),
                        downloadLog.getDownloadedAt()
                )).toList();

    }

    public void deleteDownloadLog(UUID downloadLogId, UserDetails userDetails){

        UserModel user = getUser(userDetails.getUsername());
        DownloadHistoryModel downloadLog = getDownloadLog(downloadLogId);

        downloadValidationService.validateIfUserCanDeleteDownloadLog(downloadLog, user);

        downloadHistoryRepository.delete(downloadLog);

    }

    private UserModel getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrato."));
    }

    private ConversionModel getConversion(UUID uuid){
        return conversionRepository.findById(uuid).orElseThrow(() -> new ConversionNotFoundException("Conversão não encontrada."));
    }

    private DownloadHistoryModel getDownloadLog(UUID uuid){
        return downloadHistoryRepository.findById(uuid).orElseThrow(() -> new DownloadLogNotFoundException("Registro de download não encontrado."));
    }


}
