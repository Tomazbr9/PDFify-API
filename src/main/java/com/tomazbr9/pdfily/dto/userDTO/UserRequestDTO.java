package com.tomazbr9.pdfily.dto.userDTO;

import com.tomazbr9.pdfily.enums.RoleName;

public record UserRequestDTO(
        String username,
        String email,
        String password,
        RoleName role
) {}
