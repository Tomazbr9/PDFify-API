package com.tomazbr9.pdfily.user.service;

import com.tomazbr9.pdfily.exception.UserNotFoundException;
import com.tomazbr9.pdfily.exception.UsernameAlreadyExistsException;
import com.tomazbr9.pdfily.user.model.UserModel;
import com.tomazbr9.pdfily.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserValidationsService {

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserValidationsService.class);

    public void verifyIfUsernameExists(String username){

        if (!userRepository.existsByUsername(username)){
            logger.warn("Nome de usuário já existe.");
            throw new UsernameAlreadyExistsException("Nome de usuário já existe");
        }

    }


    public UserModel getUser(String username){

        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }
}
