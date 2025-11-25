package com.tomazbr9.pdfily.conversion.service;

import com.tomazbr9.pdfily.exception.ConvertingFileException;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@DependsOn("officeManager")
public class ConversionEngineService {

    @Autowired
    DocumentConverter converter;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionEngineService.class);

    public void convert(Path input, Path output) {

        try {
            converter.convert(
                            input.toFile())
                    .to(output.toFile())
                    .execute();
        } catch (OfficeException error){
            logger.error("Erro no mecanismo de conversão", error);
            throw new ConvertingFileException("Erro no mecanismo de conversão.");
        }

    }
}
