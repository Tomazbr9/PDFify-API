package com.tomazbr9.pdfily.dto.conversionDTO;

import java.util.UUID;

public record ConvertResponseDTO(
        UUID conversionId,
        String convertedFilename,
        String status
) {}