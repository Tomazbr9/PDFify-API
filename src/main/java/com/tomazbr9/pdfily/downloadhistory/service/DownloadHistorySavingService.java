package com.tomazbr9.pdfily.downloadhistory.service;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.downloadhistory.repository.DownloadHistoryRepository;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.user.model.UserModel;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DownloadHistorySavingService {

    @Autowired
    DownloadHistoryRepository downloadHistoryRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistorySavingService.class);

    public void saveDownload(ConversionModel conversion, UserModel user){

        DownloadHistoryModel downloadHistoryModel = DownloadHistoryModel.builder()
                .conversion(conversion)
                .user(user)
                .downloadedAt(LocalDateTime.now())
                .build();

        downloadHistoryRepository.save(downloadHistoryModel);

    }
}
