package com.tomazbr9.pdfily.controller;

import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    @Autowired
    FileUploadService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails){

        FileResponseDTO response = service.uploadFile(file, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
