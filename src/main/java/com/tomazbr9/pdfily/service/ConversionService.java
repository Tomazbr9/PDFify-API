package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.enums.StatusName;
import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.model.ConversionModel;
import com.tomazbr9.pdfily.model.FileUploadModel;
import com.tomazbr9.pdfily.repository.ConversionRepository;
import com.tomazbr9.pdfily.repository.FileUploadRepository;
import org.jodconverter.local.JodConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConversionService {

    @Autowired
    FileUploadRepository fileUploadRepository;

    @Autowired
    ConversionRepository conversionRepository;

    public ConvertResponseDTO convertToPDF(ConvertRequestDTO request, UserDetails user){

        FileUploadModel fileUploadModel = fileUploadRepository.findById(request.fileId()).orElseThrow(() -> new RuntimeException("Arquivo não encontrado."));

        Path input = Paths.get(fileUploadModel.getFilePath());

        String outputFilename = UUID.randomUUID() + ".pdf";

        Path output = input.getParent().resolve(outputFilename);

        try {
            JodConverter.convert(input.toFile())
                    .to(output.toFile())
                    .execute();

            ConversionModel conversionModel = ConversionModel.builder()
                    .fileUploadModel(fileUploadModel)
                    .targetFormat(TargetFormat.PDF)
                    .status(StatusName.SUCCESS)
                    .outputPath(output.toString())
                    .createdAt(LocalDateTime.now())
                    .build();

            ConversionModel saved = conversionRepository.save(conversionModel);

            return new ConvertResponseDTO(saved.getId(), saved.getStatus().name());

        } catch (Exception error) {
            throw new RuntimeException("Erro ao converter arquivo");
        }
    }


}
