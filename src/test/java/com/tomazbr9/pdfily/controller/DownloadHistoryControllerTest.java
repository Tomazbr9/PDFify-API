package com.tomazbr9.pdfily.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.pdfily.downloadhistory.controller.DownloadHistoryController;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistoryService;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadFileDTO;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.security.filter.UserAuthenticationFilter;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DownloadHistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class DownloadHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DownloadHistoryService downloadHistoryService;

    @MockBean
    private UserAuthenticationFilter userAuthenticationFilter;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    void shouldDownloadFileSuccessfully() throws Exception {

        UUID conversionId = UUID.randomUUID();

        ByteArrayResource resource =
                new ByteArrayResource("conteudo do arquivo".getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"arquivo.pdf\"");

        DownloadFileDTO downloadFileDTO = new DownloadFileDTO(
                resource,
                headers,
                resource.contentLength()
        );

        Mockito.when(downloadHistoryService
                        .saveDownloadToHistory(any(), any()))
                .thenReturn(downloadFileDTO);

        mockMvc.perform(
                        get("/api/v1/download/{conversionId}", conversionId)
                                .with(user("bruno").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_DISPOSITION))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    void shouldReturnAllDownloadHistory() throws Exception {

        DownloadResponseDTO dto = new DownloadResponseDTO(
                UUID.randomUUID(),
                "arquivo.pdf",
                "2MB",
                LocalDateTime.now()
        );

        Mockito.when(downloadHistoryService.getAllDownloadHistory(any()))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/v1/download")
                                .with(user("bruno").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nameFile").value("arquivo.pdf"))
                .andExpect(jsonPath("$[0].sizeFile").value("2MB"));
    }


    @Test
    void shouldDeleteDownloadLogSuccessfully() throws Exception {

        UUID downloadId = UUID.randomUUID();

        Mockito.doNothing()
                .when(downloadHistoryService)
                .deleteDownloadLog(any(), any());

        mockMvc.perform(
                        delete("/api/v1/download/{downloadId}", downloadId)
                                .with(user("bruno").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
