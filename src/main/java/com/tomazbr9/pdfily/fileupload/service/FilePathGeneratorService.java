package com.tomazbr9.pdfily.fileupload.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FilePathGeneratorService {

    public Path transformInPath(String uploadDir){
        return Path.of(uploadDir);
    }

    public Path generatePath(Path dirPath, String newFileName){
        return dirPath.resolve(newFileName);
    }
}
