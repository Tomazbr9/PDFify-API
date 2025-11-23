package com.tomazbr9.pdfily.dto.downloadDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record DownloadResponseDTO(
        UUID downloadId,
        String nameFile,
        String sizeFile,
        LocalDateTime downloadDate

) {

}
