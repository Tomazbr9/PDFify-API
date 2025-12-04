package com.tomazbr9.pdfily.dto.userDTO;

import jakarta.validation.constraints.NotBlank;

public record UserPutDTO(

        @NotBlank(message = "O nome de usuário é obrigatório")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {


}
