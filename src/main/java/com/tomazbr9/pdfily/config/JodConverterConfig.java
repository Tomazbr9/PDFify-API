package com.tomazbr9.pdfily.config;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JodConverterConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalOfficeManager officeManager() {
        return LocalOfficeManager.builder()
                .officeHome("/usr/lib/libreoffice")
                .portNumbers(2002)
                .install()
                .build();
    }

    @Bean
    public DocumentConverter documentConverter(LocalOfficeManager officeManager) {
        return LocalConverter.make(officeManager);
    }
}
