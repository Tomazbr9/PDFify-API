package com.tomazbr9.pdfily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Run {

	private static final Logger logger = LoggerFactory.getLogger(Run.class);

	public static void main(String[] args) {

		logger.info("Iniciando API de conversão de documentos para PDF");

		SpringApplication.run(Run.class, args);

		logger.info("API iniciada e pronta para receber requisições");
	}





}
