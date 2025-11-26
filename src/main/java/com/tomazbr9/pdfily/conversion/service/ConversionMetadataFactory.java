package com.tomazbr9.pdfily.conversion.service;

import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.conversion.repository.ConversionRepository;
import com.tomazbr9.pdfily.enums.StatusName;
import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.fileupload.model.FileUploadModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class ConversionMetadataFactory {

    @Autowired ConversionRepository conversionRepository;

    public ConversionModel createSuccess(FileUploadModel fileUploadModel, String outputFilename, Path output, Double sizeFileInMG){

        ConversionModel conversionModel = ConversionModel.builder()
                .fileUploadModel(fileUploadModel)
                .convertedFileName(outputFilename)
                .targetFormat(TargetFormat.PDF)
                .status(StatusName.SUCCESS)
                .size(sizeFileInMG)
                .outputPath(output.toString())
                .createdAt(LocalDateTime.now())
                .build();

        return conversionRepository.save(conversionModel);

    }

    public void createFailure(FileUploadModel fileUploadModel, Path output){

        ConversionModel conversionModel = ConversionModel.builder()
                .fileUploadModel(fileUploadModel)
                .targetFormat(TargetFormat.PDF)
                .status(StatusName.FAILURE)
                .outputPath(output.toString())
                .createdAt(LocalDateTime.now())
                .build();

    }



}
