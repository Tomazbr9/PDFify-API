package com.tomazbr9.pdfily.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomazbr9.pdfily.auth.controller.AuthController;
import com.tomazbr9.pdfily.auth.service.AuthService;
import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.enums.RoleName;
import com.tomazbr9.pdfily.security.filter.UserAuthenticationFilter;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenService jwtTokenService;

    private UserRequestDTO userRequestDTO;
    private LoginDTO loginDTO;
    private JwtTokenDTO jwtTokenDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("bruno", "123456", RoleName.ROLE_USER);
        loginDTO = new LoginDTO("bruno", "123456");
        jwtTokenDTO = new JwtTokenDTO("fake-jwt-token");
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário Criado com sucesso!"));

        Mockito.verify(authService).registerUser(eq(userRequestDTO));
    }

    @Test
    void shouldAuthenticateUserAndReturnToken() throws Exception {
        Mockito.when(authService.authenticateUser(any(LoginDTO.class))).thenReturn(jwtTokenDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtTokenDTO)));

        Mockito.verify(authService).authenticateUser(eq(loginDTO));
    }
}
