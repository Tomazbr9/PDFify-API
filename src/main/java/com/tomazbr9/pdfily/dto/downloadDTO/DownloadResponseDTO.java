package com.tomazbr9.pdfily.dto.downloadDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record DownloadResponseDTO(
        UUID conversionId,
        String nameFile,
        long sizeFile,
        LocalDateTime downloadDate

) {

}
