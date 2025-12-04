package com.tomazbr9.pdfily.user.controller;

import com.tomazbr9.pdfily.dto.userDTO.UserPutDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserResponseDTO;
import com.tomazbr9.pdfily.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<UserResponseDTO> userData(@AuthenticationPrincipal UserDetails userDetails){
        UserResponseDTO response = service.userData(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> userPut(@RequestBody UserPutDTO request, @AuthenticationPrincipal UserDetails userDetails){
        UserResponseDTO response = service.userPut(request, userDetails);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> userDelete(@AuthenticationPrincipal UserDetails userDetails){

    }

}
