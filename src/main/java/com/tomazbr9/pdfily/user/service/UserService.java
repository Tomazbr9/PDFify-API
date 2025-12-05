package com.tomazbr9.pdfily.user.service;

import com.tomazbr9.pdfily.dto.userDTO.UserPutDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.exception.UsernameAlreadyExistsException;
import com.tomazbr9.pdfily.fileupload.service.FileStorageService;
import com.tomazbr9.pdfily.security.SecurityConfiguration;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidationsService userValidationsService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserResponseDTO userData(UserDetails userDetails){

        UserModel user = userValidationsService.getUser(userDetails.getUsername());

        return new UserResponseDTO(user.getUsername());

    }

    public UserResponseDTO userPut(UserPutDTO request, UserDetails userDetails){

        UserModel userModel = userValidationsService.getUser(userDetails.getUsername());

        if (!request.username().equals(userDetails.getUsername())){
            userValidationsService.verifyIfUsernameExists(request.username());
        }

        userModel.setUsername(request.username());
        userModel.setPassword(securityConfiguration.passwordEncoder().encode(request.password()));

        UserModel updatedUser = userRepository.save(userModel);

        return new UserResponseDTO(updatedUser.getUsername());

    }

    public void userDelete(UserDetails userDetails){

        UserModel userModel = userValidationsService.getUser(userDetails.getUsername());

        userRepository.delete(userModel);
    }

}
