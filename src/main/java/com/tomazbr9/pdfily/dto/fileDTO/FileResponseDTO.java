package com.tomazbr9.pdfily.dto.fileDTO;

import java.util.UUID;

public record FileResponseDTO(
   UUID id,
   String originalName
) {}