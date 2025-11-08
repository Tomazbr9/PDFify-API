package com.tomazbr9.pdfily.dto.user;

import com.tomazbr9.pdfily.enums.RoleName;

public record UserRequestDTO(
        String username,
        String email,
        String password,
        RoleName role
) {}
