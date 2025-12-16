package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.conversion.service.ConversionEngineService;
import com.tomazbr9.pdfily.conversion.service.ConversionMetadataFactory;
import com.tomazbr9.pdfily.conversion.service.ConversionService;
import com.tomazbr9.pdfily.conversion.service.ConversionValidationService;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.enums.StatusName;
import com.tomazbr9.pdfily.exception.ConvertingFileException;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import com.tomazbr9.pdfily.fileupload.repository.FileUploadRepository;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.util.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionServiceTest {

    @InjectMocks
    private ConversionService conversionService;

    @Mock
    private FileUploadRepository fileUploadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversionValidationService conversionValidationService;

    @Mock
    private ConversionEngineService conversionEngineService;

    @Mock
    private ConversionMetadataFactory conversionMetadataFactory;

    @Mock
    private UserDetails userDetails;

    @Test
    void shouldConvertFileSuccessfully() throws Exception {

        UUID fileId = UUID.randomUUID();
        UUID conversionId = UUID.randomUUID();

        ConvertRequestDTO request =
                new ConvertRequestDTO(fileId, null);

        FileUploadModel fileUpload = FileUploadModel.builder()
                .id(fileId)
                .filePath("/tmp/input.docx")
                .originalName("input.docx")
                .build();

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        Path input = Paths.get("/tmp/input.docx");
        Path output = Paths.get("/tmp/output.pdf");

        ConversionModel savedConversion = ConversionModel.builder()
                .id(conversionId)
                .status(StatusName.SUCCESS)
                .build();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(fileUploadRepository.findById(fileId))
                .thenReturn(Optional.of(fileUpload));
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.of(user));

        when(conversionMetadataFactory.createSuccess(
                eq(fileUpload),
                anyString(),
                any(Path.class),
                anyDouble()
        )).thenReturn(savedConversion);

        try (
                MockedStatic<FileUtil> fileUtilMock = mockStatic(FileUtil.class);
                MockedStatic<Files> filesMock = mockStatic(Files.class)
        ) {

            fileUtilMock.when(() -> FileUtil.generateSafeFilename("pdf"))
                    .thenReturn("output.pdf");

            filesMock.when(() -> Files.size(any(Path.class)))
                    .thenReturn(2048L);

            fileUtilMock.when(() -> FileUtil.calculateFileSizeInMB(2048L))
                    .thenReturn(2.0);

            ConvertResponseDTO response =
                    conversionService.convertToPDF(request, userDetails);

            assertNotNull(response);
            assertEquals(conversionId, response.conversionId());
            assertEquals("output.pdf", response.convertedFilename());
            assertEquals("SUCCESS", response.status());

            verify(conversionValidationService)
                    .validatedIfFileBelongsAuthenticatedUser(fileUpload, user);

            verify(conversionValidationService)
                    .validateFileExists(input);

            verify(conversionEngineService)
                    .convert(input, output);

            verify(conversionMetadataFactory)
                    .createSuccess(
                            fileUpload,
                            "output.pdf",
                            output,
                            2.0
                    );
        }
    }


    @Test
    void shouldThrowExceptionWhenConversionFails() throws Exception {

        UUID fileId = UUID.randomUUID();

        ConvertRequestDTO request =
                new ConvertRequestDTO(fileId, null);

        FileUploadModel fileUpload = FileUploadModel.builder()
                .id(fileId)
                .filePath("/tmp/input.docx")
                .originalName("input.docx")
                .build();

        UserModel user = UserModel.builder()
                .id(UUID.randomUUID())
                .username("bruno")
                .build();

        Path input = Paths.get("/tmp/input.docx");
        Path output = Paths.get("/tmp/output.pdf");

        when(userDetails.getUsername()).thenReturn("bruno");
        when(fileUploadRepository.findById(fileId))
                .thenReturn(Optional.of(fileUpload));
        when(userRepository.findByUsername("bruno"))
                .thenReturn(Optional.of(user));

        doThrow(new RuntimeException("erro"))
                .when(conversionEngineService)
                .convert(any(), any());

        try (
                MockedStatic<FileUtil> fileUtilMock = mockStatic(FileUtil.class)
        ) {

            fileUtilMock.when(() -> FileUtil.generateSafeFilename("pdf"))
                    .thenReturn("output.pdf");

            assertThrows(
                    ConvertingFileException.class,
                    () -> conversionService.convertToPDF(request, userDetails)
            );

            verify(conversionMetadataFactory)
                    .createFailure(fileUpload, output);
        }
    }
}
