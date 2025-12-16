package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.userDTO.UserPutDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.security.SecurityConfiguration;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import com.tomazbr9.pdfily.user.service.UserService;
import com.tomazbr9.pdfily.user.service.UserValidationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidationsService userValidationsService;

    @Mock
    private SecurityConfiguration securityConfiguration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetails userDetails;

    @Test
    void shouldReturnUserDataSuccessfully() {

        UserModel user = UserModel.builder()
                .id(java.util.UUID.randomUUID())
                .username("bruno")
                .build();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userValidationsService.getUser("bruno"))
                .thenReturn(user);

        UserResponseDTO response =
                userService.userData(userDetails);

        assertNotNull(response);
        assertEquals("bruno", response.username());

        verify(userValidationsService)
                .getUser("bruno");
    }

    @Test
    void shouldUpdateUserSuccessfullyWhenUsernameChanges() {

        UserModel user = UserModel.builder()
                .id(java.util.UUID.randomUUID())
                .username("bruno")
                .password("oldPassword")
                .build();

        UserPutDTO request = new UserPutDTO(
                "novoBruno",
                "novaSenha"
        );

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userValidationsService.getUser("bruno"))
                .thenReturn(user);

        when(securityConfiguration.passwordEncoder())
                .thenReturn(passwordEncoder);

        when(passwordEncoder.encode("novaSenha"))
                .thenReturn("senhaCriptografada");

        when(userRepository.save(any(UserModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response =
                userService.userPut(request, userDetails);

        assertEquals("novoBruno", response.username());

        verify(userValidationsService)
                .verifyIfUsernameExists("novoBruno");

        verify(userRepository)
                .save(user);

        assertEquals("senhaCriptografada", user.getPassword());
    }

    @Test
    void shouldUpdateUserSuccessfullyWhenUsernameDoesNotChange() {

        UserModel user = UserModel.builder()
                .id(java.util.UUID.randomUUID())
                .username("bruno")
                .password("oldPassword")
                .build();

        UserPutDTO request = new UserPutDTO(
                "bruno",
                "novaSenha"
        );

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userValidationsService.getUser("bruno"))
                .thenReturn(user);

        when(securityConfiguration.passwordEncoder())
                .thenReturn(passwordEncoder);

        when(passwordEncoder.encode("novaSenha"))
                .thenReturn("senhaCriptografada");

        when(userRepository.save(any(UserModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response =
                userService.userPut(request, userDetails);

        assertEquals("bruno", response.username());

        verify(userValidationsService, never())
                .verifyIfUsernameExists(any());

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        UserModel user = UserModel.builder()
                .id(java.util.UUID.randomUUID())
                .username("bruno")
                .build();

        when(userDetails.getUsername()).thenReturn("bruno");
        when(userValidationsService.getUser("bruno"))
                .thenReturn(user);

        userService.userDelete(userDetails);

        verify(userRepository)
                .delete(user);
    }
}
