package com.tomazbr9.pdfily.dto.conversionDTO;

import com.tomazbr9.pdfily.enums.TargetFormat;

import java.util.UUID;

public record ConvertRequestDTO(
        UUID fileId,
        TargetFormat targetFormat
) {
}
