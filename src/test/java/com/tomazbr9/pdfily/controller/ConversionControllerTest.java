package com.tomazbr9.pdfily.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.pdfily.conversion.controller.ConversionController;
import com.tomazbr9.pdfily.conversion.service.ConversionService;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.security.filter.UserAuthenticationFilter;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import com.tomazbr9.pdfily.enums.TargetFormat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConversionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ConversionService conversionService;

    @MockBean
    private UserAuthenticationFilter userAuthenticationFilter;

    @MockBean
    private JwtTokenService jwtTokenService;

    // --------------------------------------------------------
    // POST /api/v1/convert
    // --------------------------------------------------------

    @Test
    void shouldConvertFileSuccessfully() throws Exception {

        UUID fileId = UUID.randomUUID();
        UUID conversionId = UUID.randomUUID();

        ConvertRequestDTO requestDTO = new ConvertRequestDTO(
                fileId,
                TargetFormat.PDF
        );

        ConvertResponseDTO responseDTO = new ConvertResponseDTO(
                conversionId,
                "arquivo_convertido.pdf",
                "SUCCESS"
        );

        Mockito.when(conversionService.convertToPDF(any(), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(
                        post("/api/v1/convert")
                                .with(user("bruno").roles("USER"))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversionId").value(conversionId.toString()))
                .andExpect(jsonPath("$.convertedFilename").value("arquivo_convertido.pdf"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

}
