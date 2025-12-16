package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.fileupload.service.*;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @InjectMocks
    private FileUploadService fileUploadService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileUploadMetadataFactoryService fileUploadMetadataFactoryService;

    @Mock
    private FileValidationService fileValidationService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private FilePathGeneratorService filePathGeneratorService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private MultipartFile multipartFile;

    @Test
    void shouldUploadFileSuccessfully() {

        UUID fileId = UUID.randomUUID();

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        FileUploadModel savedFile = FileUploadModel.builder()
                .id(fileId)
                .originalName("arquivo.pdf")
                .build();

        Path dirPath = Paths.get("/tmp/uploads");
        Path filePath = Paths.get("/tmp/uploads/file.pdf");

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.of(user));

        when(fileValidationService.validateAndGetOriginalName(multipartFile))
                .thenReturn("arquivo.pdf");

        when(filePathGeneratorService.transformInPath(any()))
                .thenReturn(dirPath);


        when(filePathGeneratorService.generatePath(dirPath, "file.pdf"))
                .thenReturn(filePath);

        when(fileUploadMetadataFactoryService.savedFileMetaData(
                anyString(),
                any(Path.class),
                anyDouble(),
                eq(user)
        )).thenReturn(savedFile);

        try (MockedStatic<FileUtil> fileUtilMock = mockStatic(FileUtil.class)) {

            fileUtilMock.when(() -> FileUtil.getSafeExtension("arquivo.pdf"))
                    .thenReturn("pdf");

            fileUtilMock.when(() -> FileUtil.generateSafeFilename("pdf"))
                    .thenReturn("file.pdf");

            fileUtilMock.when(() -> FileUtil.calculateFileSizeInMB(anyLong()))
                    .thenReturn(1.2);

            when(multipartFile.getSize()).thenReturn(1200L);

            FileResponseDTO response =
                    fileUploadService.uploadFile(multipartFile, userDetails);

            assertNotNull(response);
            assertEquals(fileId, response.id());
            assertEquals("arquivo.pdf", response.originalName());

            verify(fileValidationService)
                    .validateFileNotEmpty(multipartFile);

            verify(fileStorageService)
                    .createDirectoryIfNeeded(dirPath);

            verify(fileStorageService)
                    .saveFileInDisk(multipartFile, filePath);
        }
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> fileUploadService.uploadFile(multipartFile, userDetails)
        );
    }
}

