package com.tomazbr9.pdfily.dto.downloadDTO;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

public record DownloadFileDTO(
        Resource resource,
        HttpHeaders headers,
        long fileSize
) {

}
