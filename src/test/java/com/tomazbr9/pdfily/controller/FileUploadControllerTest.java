package com.tomazbr9.pdfily.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.fileupload.controller.FileUploadController;
import com.tomazbr9.pdfily.fileupload.service.FileUploadService;
import com.tomazbr9.pdfily.security.filter.UserAuthenticationFilter;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileUploadController.class)
@AutoConfigureMockMvc(addFilters = false)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private UserAuthenticationFilter userAuthenticationFilter;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldUploadFileSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "documento.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "conteudo do arquivo".getBytes()
        );

        UUID fileId = UUID.randomUUID();

        FileResponseDTO responseDTO = new FileResponseDTO(
                fileId,
                "documento.pdf"
        );

        Mockito.when(fileUploadService.uploadFile(any(), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(
                        multipart("/api/v1/files/upload")
                                .file(file)
                                .with(user("bruno").roles("USER"))
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(fileId.toString()))
                .andExpect(jsonPath("$.originalName").value("documento.pdf"));
    }

    @Test
    void shouldReturnBadRequestWhenFileIsMissing() throws Exception {

        mockMvc.perform(
                        multipart("/api/v1/files/upload")
                                .with(user("bruno").roles("USER"))
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
