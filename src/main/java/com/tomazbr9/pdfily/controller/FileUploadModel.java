package com.tomazbr9.pdfily.controller;

import com.tomazbr9.pdfily.dto.user.UserResponseDTO;
import com.tomazbr9.pdfily.service.FileUploadService;
import com.tomazbr9.pdfily.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadModel {

    @Autowired
    FileUploadService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails user){



        return ResponseEntity.ok("ok");
    }



}
