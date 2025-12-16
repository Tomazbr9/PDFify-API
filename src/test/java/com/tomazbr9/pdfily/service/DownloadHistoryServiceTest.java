package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.conversion.repository.ConversionRepository;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistorySavingService;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistoryService;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadPreparationService;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadValidationService;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadFileDTO;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.downloadhistory.model.DownloadHistoryModel;
import com.tomazbr9.pdfily.downloadhistory.repository.DownloadHistoryRepository;
import com.tomazbr9.pdfily.exception.ConversionNotFoundException;
import com.tomazbr9.pdfily.exception.DownloadLogNotFoundException;
import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadHistoryServiceTest {

    @InjectMocks
    private DownloadHistoryService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionRepository conversionRepository;

    @Mock
    private DownloadHistoryRepository downloadHistoryRepository;

    @Mock
    private DownloadValidationService downloadValidationService;

    @Mock
    private DownloadHistorySavingService downloadHistorySavingService;

    @Mock
    private DownloadPreparationService downloadPreparationService;

    @Mock
    private UserDetails userDetails;

    @Test
    void shouldSaveDownloadAndReturnFileDTO() {

        UUID conversionId = UUID.randomUUID();

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        ConversionModel conversion = ConversionModel.builder()
                .id(conversionId)
                .outputPath("/tmp/arquivo.pdf")
                .convertedFileName("arquivo.pdf")
                .size(2.0)
                .build();

        Path path = Path.of("/tmp/arquivo.pdf");
        InputStreamResource resource = mock(InputStreamResource.class);
        HttpHeaders headers = new HttpHeaders();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno")).thenReturn(Optional.of(user));
        when(conversionRepository.findById(conversionId)).thenReturn(Optional.of(conversion));
        when(downloadPreparationService.convertFileToBytes(path)).thenReturn(resource);
        when(downloadPreparationService.configureHeadersHttp("arquivo.pdf")).thenReturn(headers);

        try (MockedStatic<FileUtil> fileUtilMock = mockStatic(FileUtil.class)) {

            fileUtilMock.when(() -> FileUtil.getSizeInBytes(path)).thenReturn(2048L);

            DownloadFileDTO dto =
                    service.saveDownloadToHistory(conversionId, userDetails);

            assertNotNull(dto);
            assertEquals(resource, dto.resource());
            assertEquals(headers, dto.headers());
            assertEquals(2048L, dto.fileSize());

            verify(downloadValidationService)
                    .validateIfUserCanDownload(conversion, user);

            verify(downloadHistorySavingService)
                    .saveDownload(conversion, user);
        }
    }

    @Test
    void shouldThrowExceptionWhenConversionNotFound() {

        UUID conversionId = UUID.randomUUID();

        when(conversionRepository.findById(conversionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ConversionNotFoundException.class,
                () -> service.saveDownloadToHistory(conversionId, userDetails)
        );
    }

    @Test
    void shouldReturnDownloadHistoryList() {

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        ConversionModel conversion = ConversionModel.builder()
                .convertedFileName("arquivo.pdf")
                .size(5.0)
                .build();

        DownloadHistoryModel history = DownloadHistoryModel.builder()
                .id(UUID.randomUUID())
                .conversion(conversion)
                .downloadedAt(LocalDateTime.now())
                .build();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.of(user));

        when(downloadHistoryRepository.findDownloadHistoryByUser(user))
                .thenReturn(List.of(history));

        List<DownloadResponseDTO> response =
                service.getAllDownloadHistory(userDetails);

        assertEquals(1, response.size());
        assertEquals("arquivo.pdf", response.get(0).nameFile());
        assertEquals("5.0 MB", response.get(0).sizeFile());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetHistory() {

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.getAllDownloadHistory(userDetails)
        );
    }

    @Test
    void shouldDeleteDownloadLogSuccessfully() {

        UUID downloadId = UUID.randomUUID();

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        DownloadHistoryModel downloadLog = DownloadHistoryModel.builder()
                .id(downloadId)
                .build();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.of(user));

        when(downloadHistoryRepository.findById(downloadId))
                .thenReturn(Optional.of(downloadLog));

        service.deleteDownloadLog(downloadId, userDetails);

        verify(downloadValidationService)
                .validateIfUserCanDeleteDownloadLog(downloadLog, user);

        verify(downloadHistoryRepository)
                .delete(downloadLog);
    }

    @Test
    void shouldThrowExceptionWhenDownloadLogNotFound() {

        UUID downloadId = UUID.randomUUID();

        when(downloadHistoryRepository.findById(downloadId))
                .thenReturn(Optional.empty());

        assertThrows(
                DownloadLogNotFoundException.class,
                () -> service.deleteDownloadLog(downloadId, userDetails)
        );
    }
}
