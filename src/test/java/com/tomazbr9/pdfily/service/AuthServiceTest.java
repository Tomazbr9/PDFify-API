package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.auth.service.AuthService;
import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.enums.RoleName;
import com.tomazbr9.pdfily.role.model.RoleModel;
import com.tomazbr9.pdfily.role.repository.RoleRepository;
import com.tomazbr9.pdfily.security.SecurityConfiguration;
import com.tomazbr9.pdfily.security.jwt.JwtTokenService;
import com.tomazbr9.pdfily.security.model.UserDetailsImpl;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.user.service.UserValidationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private UserValidationsService userValidationsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUserSuccessfully() {

        UUID uuid = UUID.randomUUID();

        UserRequestDTO request = new UserRequestDTO(
                "bruno",
                "123456",
                RoleName.ROLE_USER
        );

        RoleModel role = RoleModel.builder()
                .id(uuid)
                .name(RoleName.ROLE_ADMIN)
                .build();

        when(roleRepository.findByName(RoleName.ROLE_ADMIN))
                .thenReturn(Optional.of(role));

        when(securityConfiguration.passwordEncoder())
                .thenReturn(passwordEncoder);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaCriptografada");

        authService.registerUser(request);

        verify(userValidationsService)
                .verifyIfUsernameExists("bruno");

        verify(userRepository)
                .save(argThat(user ->
                        user.getUsername().equals("bruno") &&
                                user.getPassword().equals("senhaCriptografada") &&
                                user.getRoles().contains(role)
                ));
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {

        UserRequestDTO request = new UserRequestDTO(
                "bruno",
                "123456",
                RoleName.ROLE_USER
        );

        when(roleRepository.findByName(RoleName.ROLE_USER))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerUser(request)
        );

        assertEquals("Papel não encontrado", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldAuthenticateUserAndReturnJwtToken() {

        LoginDTO loginDTO = new LoginDTO(
                "bruno",
                "123456"
        );

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtTokenService.generateToken(userDetails))
                .thenReturn("jwt-token");

        JwtTokenDTO response = authService.authenticateUser(loginDTO);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(jwtTokenService)
                .generateToken(userDetails);
    }
}
