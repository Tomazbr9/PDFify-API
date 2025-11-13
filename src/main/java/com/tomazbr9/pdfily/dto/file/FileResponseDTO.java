package com.tomazbr9.pdfily.dto.file;

import java.util.UUID;

public record FileResponseDTO(
   UUID id,
   String originalName,
   String storedName
) {}
