package com.tomazbr9.pdfily.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.pdfily.dto.userDTO.UserPutDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.security.filter.UserAuthenticationFilter;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import com.tomazbr9.pdfily.user.controller.UserController;
import com.tomazbr9.pdfily.user.service.UserService;
import com.tomazbr9.pdfily.security.model.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 1. Desligamos os filtros para evitar que o Mock do filtro bloqueie a requisição
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAuthenticationFilter userAuthenticationFilter;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private UserService userService;

    // OBS: Removi UserAuthenticationFilter e JwtTokenService pois
    // com addFilters=false eles não são necessários aqui.

    private UserResponseDTO userResponse;
    private UserPutDTO updateDTO;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        // 2. IMPORTANTE: Garanta que UserResponseDTO tenha GETTERS ou @Data (Lombok)
        userResponse = new UserResponseDTO("bruno");
        updateDTO = new UserPutDTO("novoBruno", "novaSenha");

        userDetails = Mockito.mock(UserDetailsImpl.class);
        Mockito.when(userDetails.getUsername()).thenReturn("bruno");
    }

    // --------------------------------------------------------
    // GET /api/v1/user
    // --------------------------------------------------------

    @Test
    void shouldReturnAuthenticatedUser() throws Exception {
        Mockito.when(userService.userData(any()))
                .thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/user")
                        .with(user("bruno").roles("USER")))
                .andDo(print()) // Ajuda a ver o erro no console se houver
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bruno"));
    }

    // --------------------------------------------------------
    // PUT /api/v1/user
    // --------------------------------------------------------

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        UserResponseDTO updatedResponse = new UserResponseDTO(updateDTO.username());

        Mockito.when(userService.userPut(any(UserPutDTO.class), any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/user")
                        .with(user("bruno").roles("USER"))
                        .with(csrf()) // Boa prática manter
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("novoBruno"));
    }

    // --------------------------------------------------------
    // DELETE /api/v1/user
    // --------------------------------------------------------

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        Mockito.doNothing().when(userService).userDelete(any());

        mockMvc.perform(delete("/api/v1/user")
                        .with(user("bruno").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}